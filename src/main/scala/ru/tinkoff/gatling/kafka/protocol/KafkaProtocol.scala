package ru.tinkoff.gatling.kafka.protocol

import io.gatling.core.CoreComponents
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.protocol.{Protocol, ProtocolKey}

object KafkaProtocol {

  type Components = KafkaComponents

  val kafkaProtocolKey: ProtocolKey[KafkaProtocol, Components] = new ProtocolKey[KafkaProtocol, KafkaComponents] {
    override def protocolClass: Class[Protocol] =
      classOf[KafkaProtocol].asInstanceOf[Class[Protocol]]

    override def defaultProtocolValue(configuration: GatlingConfiguration): KafkaProtocol =
      throw new IllegalStateException("Can't provide a default value for KafkaProtocol")

    override def newComponents(coreComponents: CoreComponents): KafkaProtocol => KafkaComponents =
      kafkaProtocol => KafkaComponents(coreComponents, kafkaProtocol)
  }

}

case class KafkaProtocol(topic: String, properties: Map[String, Object]) extends Protocol {

  def topic(topic: String): KafkaProtocol = copy(topic = topic)

  def properties(properties: Map[String, Object]): KafkaProtocol =
    copy(properties = properties)

}
