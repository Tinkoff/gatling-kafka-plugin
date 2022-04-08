package ru.tinkoff.gatling.kafka.request.builder

import io.gatling.core.session.Expression
import org.apache.kafka.common.header.Headers

trait LowPriorSender {
  implicit def noSchemaSender[K, V]: Sender[K, V] =
    new Sender[K, V] {

      override def send(requestName: Expression[String], payload: Expression[V]): RequestBuilder[Nothing, V] =
        KafkaRequestBuilder[Nothing, V](KafkaAttributes(requestName, None, payload, None))

      override def send(
          requestName: Expression[String],
          key: Option[Expression[K]],
          payload: Expression[V],
      ): RequestBuilder[K, V] =
        KafkaRequestBuilder[K, V](KafkaAttributes(requestName, key, payload, None))

      override def send(
          requestName: Expression[String],
          key: Option[Expression[K]],
          payload: Expression[V],
          headers: Option[Expression[Headers]],
      ): RequestBuilder[K, V] =
        KafkaRequestBuilder[K, V](KafkaAttributes(requestName, key, payload, headers))
    }
}
