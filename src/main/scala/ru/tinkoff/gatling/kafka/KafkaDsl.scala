package ru.tinkoff.gatling.kafka

import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session.Expression
import ru.tinkoff.gatling.kafka.protocol.{KafkaProtocol, KafkaProtocolBuilder}
import ru.tinkoff.gatling.kafka.request.builder.{KafkaRequestBuilderBase, RequestBuilder}

trait KafkaDsl {

  val kafka: KafkaProtocolBuilder.type = KafkaProtocolBuilder

  def kafka(requestName: Expression[String]) =
    KafkaRequestBuilderBase(requestName)

  implicit def kafkaProtocolBuilder2kafkaProtocol(builder: KafkaProtocolBuilder): KafkaProtocol = builder.build

  implicit def kafkaRequestBuilder2ActionBuilder[K, V](builder: RequestBuilder[K, V]): ActionBuilder = builder.build

}
