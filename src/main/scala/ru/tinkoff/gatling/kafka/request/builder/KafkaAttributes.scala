package ru.tinkoff.gatling.kafka.request.builder

import io.gatling.core.session.Expression

case class KafkaAttributes[K, V](requestName: Expression[String], key: Option[Expression[K]], payload: Expression[V])
