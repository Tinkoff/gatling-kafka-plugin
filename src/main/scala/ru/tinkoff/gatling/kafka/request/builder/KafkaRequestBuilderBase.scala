package ru.tinkoff.gatling.kafka.request.builder

import io.gatling.core.session.Expression

case class KafkaRequestBuilderBase(requestName: Expression[String]) {

  def send[K, V](key: Expression[K], payload: Expression[V])(implicit sender: Sender[K, V]): RequestBuilder[K, V] =
    sender.send(requestName, Some(key), payload)

  def send[V](payload: Expression[V])(implicit sender: Sender[Nothing, V]): RequestBuilder[_, V] =
    sender.send(requestName, None, payload)

}
