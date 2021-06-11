package ru.tinkoff.gatling.kafka.request.builder

import io.gatling.core.session.Expression
import org.apache.kafka.common.header.Header

import java.util

case class KafkaRequestBuilderBase(requestName: Expression[String]) {

  def send[K, V](key: Expression[K], payload: Expression[V], headers: Expression[util.List[Header]])(implicit
      sender: Sender[K, V]
  ): RequestBuilder[K, V] =
    sender.send(requestName, Some(key), payload, Some(headers))

  def send[K, V](key: Expression[K], payload: Expression[V])(implicit sender: Sender[K, V]): RequestBuilder[K, V] =
    sender.send(requestName, Some(key), payload)

  def send[V](payload: Expression[V])(implicit sender: Sender[Nothing, V]): RequestBuilder[_, V] =
    sender.send(requestName, None, payload)

}
