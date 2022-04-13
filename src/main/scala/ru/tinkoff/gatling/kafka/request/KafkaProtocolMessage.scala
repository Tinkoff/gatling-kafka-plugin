package ru.tinkoff.gatling.kafka.request

import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Headers

case class KafkaProtocolMessage(
    key: Array[Byte],
    value: Array[Byte],
    inputTopic: String,
    outputTopic: String,
    headers: Option[Headers] = None,
    responseCode: Option[String] = None,
) {
  def toProducerRecord: ProducerRecord[Array[Byte], Array[Byte]] = {
    headers.fold(new ProducerRecord(inputTopic, key, value))(hs => new ProducerRecord(inputTopic, null, key, value, hs))
  }
}
