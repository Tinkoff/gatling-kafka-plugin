package ru.tinkoff.gatling.kafka.consumer.builder

import io.gatling.core.action.builder.ActionBuilder

trait PollerBuilder[K, V] {

  def build: ActionBuilder

}
