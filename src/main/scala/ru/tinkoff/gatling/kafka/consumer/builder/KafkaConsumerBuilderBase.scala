package ru.tinkoff.gatling.kafka.consumer.builder

import io.gatling.core.session.Expression

import java.time.Duration

case class KafkaConsumerBuilderBase(name: Expression[String]) {

  def poll[K, V](timeout: Duration = Duration.ofSeconds(1))(implicit
      poller: Poller[K, V]
  ): PollerBuilder[K, V] =
    poller.poll(name, timeout)

}
