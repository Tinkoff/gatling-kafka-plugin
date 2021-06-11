package ru.tinkoff.gatling.kafka.consumer.builder

import io.gatling.core.action.builder.ActionBuilder
import ru.tinkoff.gatling.kafka.actions.KafkaConsumeActionBuilder

case class KafkaConsumerBuilder[K, V](attr: ConsumerAttributes[K, V]) extends PollerBuilder[K, V] {

  def build: ActionBuilder = new KafkaConsumeActionBuilder(attr)

}
