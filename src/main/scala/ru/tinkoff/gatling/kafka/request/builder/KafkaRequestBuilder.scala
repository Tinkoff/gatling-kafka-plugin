package ru.tinkoff.gatling.kafka.request.builder

import io.gatling.core.action.builder.ActionBuilder
import ru.tinkoff.gatling.kafka.actions.KafkaRequestActionBuilder

case class KafkaRequestBuilder[K, V](attr: KafkaAttributes[K, V]) extends RequestBuilder[K, V] {

  def build: ActionBuilder = new KafkaRequestActionBuilder(attr)

}
