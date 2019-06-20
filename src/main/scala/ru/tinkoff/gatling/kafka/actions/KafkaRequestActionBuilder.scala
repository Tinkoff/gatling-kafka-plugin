package ru.tinkoff.gatling.kafka.actions

import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.structure.ScenarioContext
import org.apache.kafka.clients.producer.KafkaProducer
import ru.tinkoff.gatling.kafka.protocol.KafkaProtocol
import ru.tinkoff.gatling.kafka.request.builder.KafkaAttributes

import scala.collection.JavaConverters._

class KafkaRequestActionBuilder[K, V](attr: KafkaAttributes[K, V]) extends ActionBuilder {

  override def build(ctx: ScenarioContext, next: Action): Action = {

    import ctx._

    val kafkaComponents =
      protocolComponentsRegistry.components(KafkaProtocol.kafkaProtocolKey)

    val producer = new KafkaProducer[K, V](kafkaComponents.kafkaProtocol.properties.asJava)

    coreComponents.actorSystem.registerOnTermination(producer.close())

    new KafkaRequestAction(
      producer,
      attr,
      coreComponents,
      kafkaComponents.kafkaProtocol,
      throttled,
      next
    )
  }
}
