package ru.tinkoff.gatling.kafka.consumer.builder

import io.gatling.core.session.Expression

import java.time.Duration

trait Poller[K, V] {

  def poll(name: Expression[String], timeout: Duration): PollerBuilder[K, V]

}

object Poller {

  implicit def stdPoller[K, V]: Poller[K, V] = (name: Expression[String], timeout: Duration) =>
    KafkaConsumerBuilder[K, V](ConsumerAttributes[K, V](name, timeout))
}
