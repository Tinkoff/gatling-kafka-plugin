package ru.tinkoff.gatling.kafka.request

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Headers

case class KafkaProtocolMessage(
    key: Array[Byte],
    value: Array[Byte],
    headers: Option[Headers] = None,
    responseCode: Option[String] = None,
) {
  def toProducerRecord(topic: String): ProducerRecord[Array[Byte], Array[Byte]] = {
    headers.fold(new ProducerRecord(topic, key, value))(hs => new ProducerRecord(topic, null, key, value, hs))
  }

  def fromConsumerRecord(cr: ConsumerRecord[Array[Byte], Array[Byte]]): KafkaProtocolMessage =
    KafkaProtocolMessage(
      cr.key(),
      cr.value(),
      Option(cr.headers()),
      Option(cr.headers().lastHeader("response_code").value()).map(new String(_)),
    )
}
