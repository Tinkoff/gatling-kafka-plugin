package ru.tinkoff.gatling.kafka.actions

import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.structure.ScenarioContext
import ru.tinkoff.gatling.kafka.protocol.KafkaProtocol
import ru.tinkoff.gatling.kafka.request.builder.KafkaRequestReplyAttributes
import scala.reflect.ClassTag

class KafkaRequestReplyActionBuilder[K: ClassTag, V: ClassTag](attr: KafkaRequestReplyAttributes[K, V]) extends ActionBuilder {
  override def build(ctx: ScenarioContext, next: Action): Action = {
    val kafkaComponents = ctx.protocolComponentsRegistry.components(KafkaProtocol.kafkaProtocolKey)
    new KafkaRequestReplyAction[K, V](
      kafkaComponents,
      attr,
      ctx.coreComponents.statsEngine,
      ctx.coreComponents.clock,
      next,
    )
  }
}
