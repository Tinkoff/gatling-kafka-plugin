package ru.tinkoff.gatling.kafka.request

import org.apache.avro.generic.GenericRecord
import org.apache.kafka.common.serialization.{Serde, Serdes => JSerdes}
import org.apache.kafka.streams.kstream.WindowedSerdes
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde

import java.nio.ByteBuffer
import java.util.UUID

trait KafkaSerdesImplicits {
  implicit def stringSerde: Serde[String]                             = JSerdes.String()
  implicit def longSerde: Serde[Long]                                 = JSerdes.Long().asInstanceOf[Serde[Long]]
  implicit def javaLongSerde: Serde[java.lang.Long]                   = JSerdes.Long()
  implicit def byteArraySerde: Serde[Array[Byte]]                     = JSerdes.ByteArray()
  implicit def bytesSerde: Serde[org.apache.kafka.common.utils.Bytes] = JSerdes.Bytes()
  implicit def byteBufferSerde: Serde[ByteBuffer]                     = JSerdes.ByteBuffer()
  implicit def shortSerde: Serde[Short]                               = JSerdes.Short().asInstanceOf[Serde[Short]]
  implicit def javaShortSerde: Serde[java.lang.Short]                 = JSerdes.Short()
  implicit def floatSerde: Serde[Float]                               = JSerdes.Float().asInstanceOf[Serde[Float]]
  implicit def javaFloatSerde: Serde[java.lang.Float]                 = JSerdes.Float()
  implicit def doubleSerde: Serde[Double]                             = JSerdes.Double().asInstanceOf[Serde[Double]]
  implicit def javaDoubleSerde: Serde[java.lang.Double]               = JSerdes.Double()
  implicit def intSerde: Serde[Int]                                   = JSerdes.Integer().asInstanceOf[Serde[Int]]
  implicit def javaIntegerSerde: Serde[java.lang.Integer]             = JSerdes.Integer()
  implicit def uuidSerde: Serde[UUID]                                 = JSerdes.UUID()

  implicit def sessionWindowedSerde[T](implicit tSerde: Serde[T]): WindowedSerdes.SessionWindowedSerde[T] =
    new WindowedSerdes.SessionWindowedSerde[T](tSerde)

  implicit val avroSerde: Serde[GenericRecord] = new GenericAvroSerde()
}
