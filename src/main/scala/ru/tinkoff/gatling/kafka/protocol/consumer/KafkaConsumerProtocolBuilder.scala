package ru.tinkoff.gatling.kafka.protocol.consumer

import java.time.Duration

case object KafkaConsumerProtocolBuilder {

  def topic(name: String): KafkaConsumerProtocolBuilderPropertiesStep =
    KafkaConsumerProtocolBuilderPropertiesStep(name, Map.empty[String, Object], Duration.ofSeconds(1))

}

case class KafkaConsumerProtocolBuilder(topic: String, props: Map[String, Object], timeout: Duration) {

  def build = new KafkaConsumerProtocol(topic, props, timeout)

}
