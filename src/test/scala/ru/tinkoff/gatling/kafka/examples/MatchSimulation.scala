package ru.tinkoff.gatling.kafka.examples

import io.gatling.core.Predef._
import io.gatling.core.feeder.Feeder
import io.gatling.core.structure.ScenarioBuilder
import org.apache.kafka.clients.producer.ProducerConfig
import ru.tinkoff.gatling.kafka.Predef._
import ru.tinkoff.gatling.kafka.protocol.KafkaProtocol

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration.DurationInt

class MatchSimulation extends Simulation {
  val kafkaProtocolMatchByMessage: KafkaProtocol = kafka.requestReply
    .producerSettings(
      Map(
        ProducerConfig.ACKS_CONFIG              -> "1",
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
      ),
    )
    .consumeSettings(
      Map(
        "bootstrap.servers" -> "localhost:9092",
      ),
    )
    .timeout(5.seconds)
    .matchByMessage

  val kafkaProtocolMatchByCustomMessage: KafkaProtocol = kafka.requestReply
    .producerSettings(
      Map(
        ProducerConfig.ACKS_CONFIG              -> "1",
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
      ),
    )
    .consumeSettings(
      Map(
        "bootstrap.servers" -> "localhost:9092",
      ),
    )
    .timeout(5.seconds)
    .matchByCustomMessage("CustomMessage".getBytes)

  val c                   = new AtomicInteger(0)
  val feeder: Feeder[Int] = Iterator.continually(Map("kekey" -> c.incrementAndGet()))

  val scn: ScenarioBuilder = scenario("Basic")
    .feed(feeder)
    .exec(
      kafka("ReqRep").requestReply
        .requestTopic("test.t")
        .replyTopic("test.t")
        .send[String, String]("#{kekey}", """{ "m": "dkf" }"""),
    )

  setUp(scn.inject(atOnceUsers(1))).protocols(kafkaProtocolMatchByMessage).maxDuration(120.seconds)
}
