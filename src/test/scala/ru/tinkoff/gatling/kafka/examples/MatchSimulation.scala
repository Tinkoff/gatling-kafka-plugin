package ru.tinkoff.gatling.kafka.examples

import io.gatling.core.Predef._
import io.gatling.core.feeder.Feeder
import io.gatling.core.structure.ScenarioBuilder
import org.apache.kafka.clients.producer.ProducerConfig
import ru.tinkoff.gatling.kafka.Predef._
import ru.tinkoff.gatling.kafka.protocol.KafkaProtocol
import ru.tinkoff.gatling.kafka.request.KafkaProtocolMessage

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration.DurationInt

class MatchSimulation extends Simulation {

  val kafkaProtocolMatchByValue: KafkaProtocol = kafka.requestReply
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
    // for match by message value
    .matchByValue

  val kafkaProtocolMatchByKeyExpectMultipleMessagesInResponse: KafkaProtocol = kafka.requestReply
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
    // each request message is matched by 3 replies, first two matched messages will be skipped and the third one recorded
    .skipMatches(2)

  def matchByOwnVal(message: KafkaProtocolMessage): Array[Byte] = {
    // do something with the message and extract the values your are interested in
    // method is called:
    // - for each message which will be sent out
    // - for each message which has been received
    "Custom Message".getBytes // just returning something
  }

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
    .matchByMessage(matchByOwnVal)

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
