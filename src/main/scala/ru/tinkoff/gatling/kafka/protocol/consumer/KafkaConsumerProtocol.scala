package ru.tinkoff.gatling.kafka.protocol.consumer

import io.gatling.core.CoreComponents
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.protocol.{Protocol, ProtocolKey}

import java.time.Duration


object KafkaConsumerProtocol {

  type Components = KafkaConsumerComponents

  val kafkaProtocolKey: ProtocolKey[KafkaConsumerProtocol, Components] = new ProtocolKey[KafkaConsumerProtocol, KafkaConsumerComponents] {
    override def protocolClass: Class[Protocol] =
      classOf[KafkaConsumerProtocol].asInstanceOf[Class[Protocol]]

    override def defaultProtocolValue(configuration: GatlingConfiguration): KafkaConsumerProtocol =
      throw new IllegalStateException("Can't provide a default value for KafkaProtocol")

    override def newComponents(coreComponents: CoreComponents): KafkaConsumerProtocol => KafkaConsumerComponents =
      kafkaConsumerProtocol => KafkaConsumerComponents(coreComponents, kafkaConsumerProtocol)
  }

}

case class KafkaConsumerProtocol(topic: String, properties: Map[String, Object], timeout: Duration)
    extends Protocol {

  def topic(topic: String): KafkaConsumerProtocol = copy(topic = topic)

  def properties(properties: Map[String, Object]): KafkaConsumerProtocol =
    copy(properties = properties)

  def timeout(timeout: Duration): KafkaConsumerProtocol = copy(timeout = timeout)

}
