package ru.tinkoff.gatling.kafka.consumer.builder

import io.gatling.core.session.Expression

import java.time.Duration

case class ConsumerAttributes[K, V](
    name: Expression[String],
    timeout: Duration
)
