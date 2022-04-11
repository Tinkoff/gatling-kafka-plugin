package ru.tinkoff.gatling.kafka.actions

import com.softwaremill.quicklens.ModifyPimp
import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.structure.ScenarioContext
import ru.tinkoff.gatling.kafka.KafkaCheck
import ru.tinkoff.gatling.kafka.protocol.KafkaProtocol
import ru.tinkoff.gatling.kafka.request.builder.KafkaRequestReplyAttributes

import scala.reflect.ClassTag

case class KafkaRequestReplyActionBuilder[K: ClassTag, V: ClassTag](attributes: KafkaRequestReplyAttributes[K, V])
    extends ActionBuilder {
  def check(checks: KafkaCheck*): KafkaRequestReplyActionBuilder[K, V] =
    this.modify(_.attributes.checks).using(_ ::: checks.toList)

  override def build(ctx: ScenarioContext, next: Action): Action = {
    val kafkaComponents = ctx.protocolComponentsRegistry.components(KafkaProtocol.kafkaProtocolKey)
    new KafkaRequestReplyAction[K, V](
      kafkaComponents,
      attributes,
      ctx.coreComponents.statsEngine,
      ctx.coreComponents.clock,
      next,
    )
  }
}
