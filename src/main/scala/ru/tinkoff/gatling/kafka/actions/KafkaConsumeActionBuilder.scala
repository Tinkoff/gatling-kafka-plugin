package ru.tinkoff.gatling.kafka.actions

import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.structure.ScenarioContext
import org.apache.kafka.clients.consumer.KafkaConsumer
import ru.tinkoff.gatling.kafka.consumer.builder.ConsumerAttributes
import ru.tinkoff.gatling.kafka.protocol.consumer.KafkaConsumerProtocol

import scala.jdk.CollectionConverters._

class KafkaConsumeActionBuilder[K, V](attr: ConsumerAttributes[K, V]) extends ActionBuilder {

  override def build(ctx: ScenarioContext, next: Action): Action = {

    import ctx._

    val kafkaComponents =
      protocolComponentsRegistry.components(KafkaConsumerProtocol.kafkaProtocolKey)

    val consumer = new KafkaConsumer[K, V](kafkaComponents.kafkaProtocol.properties.asJava)

    coreComponents.actorSystem.registerOnTermination(consumer.close())

    new KafkaConsumeAction(
      consumer,
      attr,
      coreComponents,
      kafkaComponents.kafkaProtocol,
      throttled,
      next
    )
  }
}
