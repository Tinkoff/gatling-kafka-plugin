package ru.tinkoff.gatling.kafka.request.builder

import io.gatling.core.action.builder.ActionBuilder
import ru.tinkoff.gatling.kafka.actions.KafkaRequestAvro4sActionBuilder

case class KafkaAvro4sRequestBuilder[K, V](attr: Avro4sAttributes[K, V]) extends RequestBuilder[K, V] {

  def build: ActionBuilder = new KafkaRequestAvro4sActionBuilder(attr)

}
