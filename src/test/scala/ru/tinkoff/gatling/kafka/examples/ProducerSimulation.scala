package ru.tinkoff.gatling.kafka.examples

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import org.apache.kafka.clients.producer.ProducerConfig
import ru.tinkoff.gatling.kafka.Predef._
import ru.tinkoff.gatling.kafka.protocol.KafkaProtocol

class ProducerSimulation extends Simulation {

  val kafkaConsumerConf: KafkaProtocol =
    kafka
      .topic("test.topic")
      .properties(
        Map(
          ProducerConfig.ACKS_CONFIG                   -> "1",
          ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG   -> "org.apache.kafka.common.serialization.StringSerializer",
          ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.DoubleSerializer",
          ProducerConfig.BOOTSTRAP_SERVERS_CONFIG      -> "localhost:9092",
        ),
      )

  val scn: ScenarioBuilder = scenario("Basic")
    .exec(
      kafka("BasicRequest")
        .send[Double](1.16423),
    )
    .exec(kafka("BasicRequestWithKey").send[String, Double]("true", 12.0))

  setUp(scn.inject(atOnceUsers(5))).protocols(kafkaConsumerConf)

}
