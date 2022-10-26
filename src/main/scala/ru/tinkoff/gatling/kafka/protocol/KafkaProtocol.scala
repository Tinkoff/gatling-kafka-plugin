package ru.tinkoff.gatling.kafka.protocol

import io.gatling.core.CoreComponents
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.protocol.{Protocol, ProtocolKey}
import ru.tinkoff.gatling.kafka.client.{KafkaSender, TrackersPool}
import ru.tinkoff.gatling.kafka.protocol.KafkaProtocol.KafkaMatcher
import ru.tinkoff.gatling.kafka.request.KafkaProtocolMessage
import io.gatling.core.session.Expression
import io.gatling.core.session.el._

import java.util.concurrent.Executors
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

object KafkaProtocol {
  /*
  Matcher description
  def isKeyMatch - boolean val for correct key/value serialization in KafkaRequestReplyAction
  !Impractical! isKeyMatch need to replace, or change
  def requestMatch - expression, that will be converted to array[byte] and than used as messageSent hashmap key
  def responseMatch - value, that will be used in received message in messageSent hashmap comparison
   */
  trait KafkaMatcher {
    def isKeyMatch: Boolean                                      = true
    def requestMatch(msg: KafkaProtocolMessage): Expression[Any] = new String(msg.key).el[String]
    def responseMatch(msg: KafkaProtocolMessage): Array[Byte]    = msg.key
  }

  object KafkaKeyMatcher extends KafkaMatcher {
    override def requestMatch(msg: KafkaProtocolMessage): Expression[String] = new String(msg.key).el[String]
    override def responseMatch(msg: KafkaProtocolMessage): Array[Byte]       = msg.key
  }

  object KafkaMessageMatcher extends KafkaMatcher {
    override def isKeyMatch: Boolean                                         = false
    override def requestMatch(msg: KafkaProtocolMessage): Expression[String] = new String(msg.value).el[String]
    override def responseMatch(msg: KafkaProtocolMessage): Array[Byte]       = msg.value
  }

  case class KafkaCustomMessageMatcher(customMessage: Expression[Any]) extends KafkaMatcher {
    override def isKeyMatch: Boolean                                      = false
    override def requestMatch(msg: KafkaProtocolMessage): Expression[Any] = customMessage
    override def responseMatch(msg: KafkaProtocolMessage): Array[Byte]    = msg.value
  }

  case class KafkaCustomKeyMatcher(customKey: Expression[Any]) extends KafkaMatcher {
    override def requestMatch(msg: KafkaProtocolMessage): Expression[Any] = customKey
    override def responseMatch(msg: KafkaProtocolMessage): Array[Byte]    = msg.key
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
