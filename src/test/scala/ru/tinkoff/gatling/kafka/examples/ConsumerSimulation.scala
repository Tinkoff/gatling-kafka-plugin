package ru.tinkoff.gatling.kafka.examples

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import org.apache.kafka.clients.producer.ProducerConfig
import ru.tinkoff.gatling.kafka.Predef._
import ru.tinkoff.gatling.kafka.protocol.KafkaProtocol

class ConsumerSimulation extends Simulation {

  val kafkaConsumerConf: KafkaProtocol =
    kafka
      .topic("test.topic")
      .properties(Map(ProducerConfig.ACKS_CONFIG -> "1"))

  val scn: ScenarioBuilder             = scenario("Basic")
    .exec(
      kafka("BasicRequest")
        .send[String]("foo")
    )

}
