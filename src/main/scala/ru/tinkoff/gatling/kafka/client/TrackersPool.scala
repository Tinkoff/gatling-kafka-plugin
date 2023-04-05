package ru.tinkoff.gatling.kafka.client

import akka.actor.{ActorSystem, CoordinatedShutdown}
import io.gatling.commons.util.Clock
import io.gatling.core.stats.StatsEngine
import io.gatling.core.util.NameGen
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.serialization.Serdes._
import ru.tinkoff.gatling.kafka.KafkaLogging
import ru.tinkoff.gatling.kafka.client.KafkaMessageTrackerActor.MessageConsumed
import ru.tinkoff.gatling.kafka.protocol.KafkaProtocol.KafkaMatcher
import ru.tinkoff.gatling.kafka.request.KafkaProtocolMessage

import java.util.concurrent.ConcurrentHashMap
import scala.jdk.CollectionConverters._

class TrackersPool(
    streamsSettings: Map[String, AnyRef],
    system: ActorSystem,
    statsEngine: StatsEngine,
    clock: Clock,
) extends KafkaLogging with NameGen {

  private val trackers = new ConcurrentHashMap[String, KafkaMessageTracker]
  private val props    = new java.util.Properties()
  props.putAll(streamsSettings.asJava)

  def tracker(
      inputTopic: String,
      outputTopic: String,
      messageMatcher: KafkaMatcher,
      responseTransformer: Option[KafkaProtocolMessage => KafkaProtocolMessage],
  ): KafkaMessageTracker =
    trackers.computeIfAbsent(
      outputTopic,
      _ => {
        val actor =
          system.actorOf(KafkaMessageTrackerActor.props(statsEngine, clock), genName("kafkaTrackerActor"))

        val builder = new StreamsBuilder

        builder.stream[Array[Byte], Array[Byte]](outputTopic).foreach { case (k, v) =>
          val message = KafkaProtocolMessage(k, v, inputTopic, outputTopic)
          if (messageMatcher.responseMatch(message) == null) {
            logger.error(s"no messageMatcher key for read message ${message} in inputTopic ${inputTopic}, outputTopic ${outputTopic}")
          } else {
            if (k == null || v == null)
              logger.info(s" --- received message with null key or value")
            else
              logger.info(s" --- received  ${new String(k)} ${new String(v)}")
            val receivedTimestamp = clock.nowMillis
            val replyId           = messageMatcher.responseMatch(message)
            if (k != null)
              logMessage(
                s"Record received key=${new String(k)}",
                message,
              )
            else
              logMessage(
                s"Record received key=null",
                message,
              )

            actor ! MessageConsumed(
              replyId,
              receivedTimestamp,
              responseTransformer.map(_(message)).getOrElse(message),
            )
          }
        }

        val streams = new KafkaStreams(builder.build(), props)

        streams.cleanUp()
        streams.start()

        CoordinatedShutdown(system).addJvmShutdownHook(streams.close())

        new KafkaMessageTracker(actor)
      },
    )
}
