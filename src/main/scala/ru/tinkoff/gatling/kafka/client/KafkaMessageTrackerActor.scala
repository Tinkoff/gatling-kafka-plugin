package ru.tinkoff.gatling.kafka.client

import akka.actor.{Actor, Props, Timers}
import com.typesafe.scalalogging.LazyLogging
import io.gatling.commons.stats.{KO, OK, Status}
import io.gatling.commons.util.Clock
import io.gatling.commons.validation.Failure
import io.gatling.core.action.Action
import io.gatling.core.check.Check
import io.gatling.core.session.Session
import io.gatling.core.stats.StatsEngine
import ru.tinkoff.gatling.kafka.KafkaCheck
import ru.tinkoff.gatling.kafka.request.KafkaProtocolMessage

import scala.collection.mutable
import scala.concurrent.duration.DurationInt

object KafkaMessageTrackerActor {

  def props(statsEngine: StatsEngine, clock: Clock): Props =
    Props(new KafkaMessageTrackerActor(statsEngine, clock))

  case class MessagePublished(
      key: Array[Byte],
      sent: Long,
      replyTimeout: Long,
      checks: List[KafkaCheck],
      session: Session,
      next: Action,
      requestName: String,
  )

  case class MessageConsumed(
      received: Long,
      message: KafkaProtocolMessage,
  )

  case object TimeoutScan

  private def makeKeyForSentMessages(m: Array[Byte]): String =
    Option(m).map(java.util.Base64.getEncoder.encodeToString).getOrElse("")
}

class KafkaMessageTrackerActor(statsEngine: StatsEngine, clock: Clock) extends Actor with Timers with LazyLogging {
  import KafkaMessageTrackerActor._
  def triggerPeriodicTimeoutScan(
      periodicTimeoutScanTriggered: Boolean,
      sentMessages: mutable.HashMap[String, MessagePublished],
      timedOutMessages: mutable.ArrayBuffer[MessagePublished],
  ): Unit =
    if (!periodicTimeoutScanTriggered) {
      context.become(onMessage(periodicTimeoutScanTriggered = true, sentMessages, timedOutMessages))
      timers.startTimerWithFixedDelay("timeoutTimer", TimeoutScan, 1000.millis)
    }

  override def receive: Receive =
    onMessage(
      periodicTimeoutScanTriggered = false,
      mutable.HashMap.empty[String, MessagePublished],
      mutable.ArrayBuffer.empty[MessagePublished],
    )

  private def executeNext(
      session: Session,
      sent: Long,
      received: Long,
      status: Status,
      next: Action,
      requestName: String,
      responseCode: Option[String],
      message: Option[String],
  ): Unit = {
    statsEngine.logResponse(
      session.scenario,
      session.groups,
      requestName,
      sent,
      received,
      status,
      responseCode,
      message,
    )
    next ! session.logGroupRequestTimings(sent, received)
  }

  /** Processes a matched message
    */
  private def processMessage(
      session: Session,
      sent: Long,
      received: Long,
      checks: List[KafkaCheck],
      message: KafkaProtocolMessage,
      next: Action,
      requestName: String,
  ): Unit = {
    val (newSession, error) = Check.check(message, session, checks)
    error match {
      case Some(Failure(errorMessage)) =>
        executeNext(
          newSession.markAsFailed,
          sent,
          received,
          KO,
          next,
          requestName,
          message.responseCode,
          Some(errorMessage),
        )
      case _                           =>
        executeNext(newSession, sent, received, OK, next, requestName, None, None)
    }
  }

  private def onMessage(
      periodicTimeoutScanTriggered: Boolean,
      sentMessages: mutable.HashMap[String, MessagePublished],
      timedOutMessages: mutable.ArrayBuffer[MessagePublished],
  ): Receive = {
    // message was sent; add the timestamps to the map
    case messageSent: MessagePublished      =>
      val key = KafkaMessageTrackerActor.makeKeyForSentMessages(messageSent.key)
      sentMessages += key -> messageSent
      if (messageSent.replyTimeout > 0) {
        triggerPeriodicTimeoutScan(periodicTimeoutScanTriggered, sentMessages, timedOutMessages)
      }

    // message was received; publish stats and remove from the map
    case MessageConsumed(received, message) =>
      // if key is missing, message was already acked and is a dup, or request timeout
      val key = KafkaMessageTrackerActor.makeKeyForSentMessages(message.key)
      sentMessages.remove(key).foreach { case MessagePublished(_, sent, _, checks, session, next, requestName) =>
        processMessage(session, sent, received, checks, message, next, requestName)
      }

    case TimeoutScan =>
      val now = clock.nowMillis
      sentMessages.valuesIterator.foreach { messagePublished =>
        val replyTimeout = messagePublished.replyTimeout
        if (replyTimeout > 0 && (now - messagePublished.sent) > replyTimeout) {
          timedOutMessages += messagePublished
        }
      }

      for (MessagePublished(matchId, sent, receivedTimeout, _, session, next, requestName) <- timedOutMessages) {
        sentMessages.remove(KafkaMessageTrackerActor.makeKeyForSentMessages(matchId))
        executeNext(
          session.markAsFailed,
          sent,
          now,
          KO,
          next,
          requestName,
          None,
          Some(s"Reply timeout after $receivedTimeout ms"),
        )
      }
      timedOutMessages.clear()
  }
}
