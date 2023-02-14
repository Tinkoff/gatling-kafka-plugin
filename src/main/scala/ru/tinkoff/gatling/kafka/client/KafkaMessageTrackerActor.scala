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

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable
import scala.concurrent.duration.DurationInt

object KafkaMessageTrackerActor {

  def props(statsEngine: StatsEngine, clock: Clock): Props =
    Props(new KafkaMessageTrackerActor(statsEngine, clock))

  case class MessagePublished(
      matchId: Array[Byte],
      sent: Long,
      replyTimeout: Long,
      checks: List[KafkaCheck],
      session: Session,
      next: Action,
      requestName: String,
      skipMessages: Int,
  )

  case class MessageConsumed(
      replyId: Array[Byte],
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
      skipMessages: mutable.HashMap[String, AtomicInteger],
      timedOutMessages: mutable.ArrayBuffer[MessagePublished],
  ): Unit =
    if (!periodicTimeoutScanTriggered) {
      context.become(onMessage(periodicTimeoutScanTriggered = true, sentMessages, skipMessages, timedOutMessages))
      timers.startTimerWithFixedDelay("timeoutTimer", TimeoutScan, 1000.millis)
    }

  override def receive: Receive =
    onMessage(
      periodicTimeoutScanTriggered = false,
      mutable.HashMap.empty[String, MessagePublished],
      mutable.HashMap.empty[String, AtomicInteger],
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
      skipMessages: mutable.HashMap[String, AtomicInteger],
      timedOutMessages: mutable.ArrayBuffer[MessagePublished],
  ): Receive = {
    // message was sent; add the timestamps to the map
    case messageSent: MessagePublished               =>
      val key = makeKeyForSentMessages(messageSent.matchId)
      sentMessages += key -> messageSent
      skipMessages += key -> new AtomicInteger(messageSent.skipMessages)
      if (messageSent.replyTimeout > 0) {
        triggerPeriodicTimeoutScan(periodicTimeoutScanTriggered, sentMessages, skipMessages, timedOutMessages)
      }

    // message was received; publish stats and remove from the map
    case MessageConsumed(replyId, received, message) =>
      // if key is missing, message was already acked and is a dup, or request timeout
      val key = makeKeyForSentMessages(replyId)

      if (skipMessages.contains(key))
        if (skipMessages(key).get() == 0) {
          logger.debug(s"processing match for key $key")
          skipMessages.remove(key)
          sentMessages.remove(key).foreach { case MessagePublished(_, sent, _, checks, session, next, requestName, _) =>
            processMessage(session, sent, received, checks, message, next, requestName)
          }
        } else {
          skipMessages(key).decrementAndGet()
          logger.debug(s"skipping match for key $key, left ${skipMessages(key).get()} matches")
        }
      else
        logger.debug(s"skipping processing message with key $key as it doesn't exist in skipMessages")

    case TimeoutScan =>
      val now = clock.nowMillis
      sentMessages.valuesIterator.foreach { messagePublished =>
        val replyTimeout = messagePublished.replyTimeout
        if (replyTimeout > 0 && (now - messagePublished.sent) > replyTimeout) {
          timedOutMessages += messagePublished
        }
      }
      for (MessagePublished(matchId, sent, receivedTimeout, _, session, next, requestName, _) <- timedOutMessages) {
        val k = makeKeyForSentMessages(matchId)
        sentMessages.remove(k)
        skipMessages.remove(k)
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
