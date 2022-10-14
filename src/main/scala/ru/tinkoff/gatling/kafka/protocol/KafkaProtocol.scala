package ru.tinkoff.gatling.kafka.protocol

import io.gatling.core.CoreComponents
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.protocol.{Protocol, ProtocolKey}
import ru.tinkoff.gatling.kafka.client.{KafkaSender, TrackersPool}
import ru.tinkoff.gatling.kafka.protocol.KafkaProtocol.KafkaMatcher
import ru.tinkoff.gatling.kafka.request.KafkaProtocolMessage

import java.util.concurrent.Executors
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

object KafkaProtocol {

  trait KafkaMatcher {
    def requestMatch(msg: KafkaProtocolMessage): Array[Byte]  = msg.key
    def responseMatch(msg: KafkaProtocolMessage): Array[Byte] = msg.key
  }

  object KafkaKeyMatcher extends KafkaMatcher {
    override def requestMatch(msg: KafkaProtocolMessage): Array[Byte]  = msg.key
    override def responseMatch(msg: KafkaProtocolMessage): Array[Byte] = msg.key
  }

  object KafkaMessageMatcher extends KafkaMatcher {
    override def requestMatch(msg: KafkaProtocolMessage): Array[Byte]  = msg.value
    override def responseMatch(msg: KafkaProtocolMessage): Array[Byte] = msg.value
  }

  case class KafkaCustomMessageMatcher(customMessage: Array[Byte]) extends KafkaMatcher {
    override def requestMatch(msg: KafkaProtocolMessage): Array[Byte]  = customMessage
    override def responseMatch(msg: KafkaProtocolMessage): Array[Byte] = msg.value
  }

  type Components = KafkaComponents

  val kafkaProtocolKey: ProtocolKey[KafkaProtocol, Components] = new ProtocolKey[KafkaProtocol, Components] {
    override def protocolClass: Class[Protocol] =
      classOf[KafkaProtocol].asInstanceOf[Class[Protocol]]

    override def defaultProtocolValue(configuration: GatlingConfiguration): KafkaProtocol =
      throw new IllegalStateException("Can't provide a default value for KafkaProtocol")

    override def newComponents(coreComponents: CoreComponents): KafkaProtocol => KafkaComponents =
      kafkaProtocol => {
        val blockingPool                                 = Executors.newCachedThreadPool()
        implicit val ec: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(blockingPool)

        val sender       = KafkaSender(kafkaProtocol.producerProperties)
        val trackersPool = new TrackersPool(
          kafkaProtocol.consumeProperties,
          coreComponents.actorSystem,
          coreComponents.statsEngine,
          coreComponents.clock,
        )

        KafkaComponents(coreComponents, kafkaProtocol, trackersPool, sender)
      }
  }
}

case class KafkaProtocol(
    producerTopic: String,
    producerProperties: Map[String, AnyRef],
    consumeProperties: Map[String, AnyRef],
    timeout: FiniteDuration,
    messageMatcher: KafkaMatcher,
) extends Protocol {

  def topic(t: String): KafkaProtocol = copy(producerTopic = t)

  def properties(properties: Map[String, AnyRef]): KafkaProtocol =
    copy(producerProperties = properties)

  def producerProperties(properties: Map[String, AnyRef]): KafkaProtocol = copy(producerProperties = properties)
  def consumeProperties(properties: Map[String, AnyRef]): KafkaProtocol  = copy(consumeProperties = properties)
  def timeout(t: FiniteDuration): KafkaProtocol                          = copy(timeout = t)

}
