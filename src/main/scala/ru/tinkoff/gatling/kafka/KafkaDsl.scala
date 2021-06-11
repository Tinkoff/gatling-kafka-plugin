package ru.tinkoff.gatling.kafka

import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session.Expression
import ru.tinkoff.gatling.kafka.consumer.builder.KafkaConsumerBuilderBase
import ru.tinkoff.gatling.kafka.protocol.consumer.KafkaConsumerProtocolBuilder
import ru.tinkoff.gatling.kafka.protocol.{KafkaProtocol, KafkaProtocolBuilder}
import ru.tinkoff.gatling.kafka.request.builder.{KafkaRequestBuilderBase, RequestBuilder}

trait KafkaDsl {

  val kafka: KafkaProtocolBuilder.type = KafkaProtocolBuilder

  val kafkaConsumer: KafkaConsumerProtocolBuilder.type = KafkaConsumerProtocolBuilder

  def kafkaConsumer(name: Expression[String]): KafkaConsumerBuilderBase =
    KafkaConsumerBuilderBase(name)

  def kafka(requestName: Expression[String]): KafkaRequestBuilderBase =
    KafkaRequestBuilderBase(requestName)

  implicit def kafkaProtocolBuilder2kafkaProtocol(builder: KafkaProtocolBuilder): KafkaProtocol = builder.build

  implicit def kafkaRequestBuilder2ActionBuilder[K, V](builder: RequestBuilder[K, V]): ActionBuilder = builder.build

}
