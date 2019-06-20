package ru.tinkoff.gatling.kafka.request.builder

import io.gatling.core.session.Expression

trait LowPriorSender {
  implicit def noSchemaSender[K, V]: Sender[K, V] =
    new Sender[K, V] {

      override def send(requestName: Expression[String], payload: Expression[V]): RequestBuilder[Nothing, V] =
        KafkaRequestBuilder[Nothing, V](KafkaAttributes(requestName, None, payload))

      override def send(requestName: Expression[String],
                        key: Option[Expression[K]],
                        payload: Expression[V]): RequestBuilder[K, V] =
        KafkaRequestBuilder[K, V](KafkaAttributes(requestName, key, payload))
    }
}
