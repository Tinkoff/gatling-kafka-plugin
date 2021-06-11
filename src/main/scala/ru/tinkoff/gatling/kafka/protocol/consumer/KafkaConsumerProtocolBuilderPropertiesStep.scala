package ru.tinkoff.gatling.kafka.protocol.consumer

import java.time.Duration

case class KafkaConsumerProtocolBuilderPropertiesStep(topic: String, props: Map[String, Object], timeout: Duration) {

  def properties(props: Map[String, Object]): KafkaConsumerProtocolBuilder =
    KafkaConsumerProtocolBuilder(topic, props, timeout)

  def timeout(timeout: Duration): KafkaConsumerProtocolBuilder =
    KafkaConsumerProtocolBuilder(topic, props, timeout)

}
