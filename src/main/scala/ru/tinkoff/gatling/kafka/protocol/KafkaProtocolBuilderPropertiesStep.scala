package ru.tinkoff.gatling.kafka.protocol

case class KafkaProtocolBuilderPropertiesStep(topic: String, props: Map[String, Object]) {

  def properties(props: Map[String, Object]): KafkaProtocolBuilder =
    KafkaProtocolBuilder(topic, props)
}
