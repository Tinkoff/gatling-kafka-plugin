package ru.tinkoff.gatling.kafka.actions

import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.structure.ScenarioContext
import io.gatling.core.util.NameGen
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.KafkaProducer
import ru.tinkoff.gatling.kafka.protocol.{KafkaComponents, KafkaProtocol}
import ru.tinkoff.gatling.kafka.request.builder.Avro4sAttributes

import scala.collection.JavaConverters._

class KafkaRequestAvro4sActionBuilder[K, V](attr: Avro4sAttributes[K, V]) extends ActionBuilder with NameGen {
  override def build(ctx: ScenarioContext, next: Action): Action = {
    import ctx._

    val kafkaComponents: KafkaComponents = protocolComponentsRegistry.components(KafkaProtocol.kafkaProtocolKey)

    val producer = new KafkaProducer[K, GenericRecord](kafkaComponents.kafkaProtocol.properties.asJava)

    coreComponents.actorSystem.registerOnTermination(producer.close())

    new KafkaAvro4sRequestAction(
      producer,
      attr,
      coreComponents,
      kafkaComponents.kafkaProtocol,
      throttled,
      next
    )

  }
}
