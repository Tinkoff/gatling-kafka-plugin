package ru.tinkoff.gatling.kafka.examples

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.header.internals.RecordHeaders
import ru.tinkoff.gatling.kafka.javaapi.KafkaDsl._

class KafkaJavaapiMethodsGatlingTest extends Simulation {

  val kafkaConfwoKey = kafka
    .topic("myTopic3")
    .properties(
      java.util.Map.of(
        ProducerConfig.ACKS_CONFIG,
        "1",
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
        "localhost:9093",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer",
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer",
      ),
    )
    .protocol()

  setUp(
    scenario("Request String without key")
      .exec(
        kafka("Request String without headers and key")
          .send("testJavaWithoutKeyAndHeaders")
          .asScala(),
      )
      .exec(
        kafka("Request String with headers without key")
          .send("testJavaWithHeadersWithoutKey", new RecordHeaders().add("test-header", "test_value".getBytes()))
          .asScala(),
      )
      .inject(nothingFor(1), atOnceUsers(1))
      .protocols(kafkaConfwoKey),
  )

}
