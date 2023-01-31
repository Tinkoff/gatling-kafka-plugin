package ru.tinkoff.gatling.kafka.javaapi.checks

import io.gatling.core.check.Check
import io.gatling.core.check._
import io.gatling.core.check.bytes.BodyBytesCheckType
import io.gatling.core.check.jmespath.JmesPathCheckType
import io.gatling.core.check.jsonpath.JsonPathCheckType
import io.gatling.core.check.string.BodyStringCheckType
import io.gatling.core.check.substring.SubstringCheckType
import io.gatling.core.check.xpath.XPathCheckType
import io.gatling.javaapi.core.internal.CoreCheckType
import ru.tinkoff.gatling.kafka.checks.{KafkaCheckMaterializer, KafkaCheckSupport}
import ru.tinkoff.gatling.kafka.request.KafkaProtocolMessage
import ru.tinkoff.gatling.kafka.{KafkaCheck, checks}
import net.sf.saxon.s9api.XdmNode
import com.fasterxml.jackson.databind.JsonNode
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.common.serialization.Serde

import java.{util => ju}
import scala.jdk.CollectionConverters._

object KafkaChecks {
  case class KafkaCheckTypeWrapper(value: Check[KafkaProtocolMessage])
  class SimpleChecksScala extends KafkaCheckSupport {}

  val avroSerde: Serde[GenericRecord] = new GenericAvroSerde()

  private def toScalaCheck(javaCheck: Object): KafkaCheck = {
    javaCheck match {
      case _: io.gatling.javaapi.core.CheckBuilder =>
        val checkBuilder = javaCheck.asInstanceOf[io.gatling.javaapi.core.CheckBuilder]
        val scalaCheck = checkBuilder.asScala
        checkBuilder.`type` match {
          case CoreCheckType.BodyBytes =>
            scalaCheck
              .asInstanceOf[CheckBuilder[BodyBytesCheckType, Array[Byte]]]
              .build(KafkaCheckMaterializer.bodyBytes)
          case CoreCheckType.BodyString =>
            scalaCheck
              .asInstanceOf[CheckBuilder[BodyStringCheckType, String]]
              .build(KafkaCheckMaterializer.bodyString(io.gatling.core.Predef.configuration))
          case CoreCheckType.Substring =>
            scalaCheck
              .asInstanceOf[CheckBuilder[SubstringCheckType, String]]
              .build(KafkaCheckMaterializer.substring(io.gatling.core.Predef.configuration))
          case CoreCheckType.XPath =>
            scalaCheck
              .asInstanceOf[CheckBuilder[XPathCheckType, XdmNode]]
              .build(KafkaCheckMaterializer.xpath(io.gatling.core.Predef.configuration))
          case CoreCheckType.JsonPath =>
            scalaCheck
              .asInstanceOf[CheckBuilder[JsonPathCheckType, JsonNode]]
              .build(
                KafkaCheckMaterializer.jsonPath(io.gatling.core.Predef.defaultJsonParsers, io.gatling.core.Predef.configuration),
              )
          case CoreCheckType.JmesPath =>
            scalaCheck
              .asInstanceOf[CheckBuilder[JmesPathCheckType, JsonNode]]
              .build(
                KafkaCheckMaterializer.jmesPath(io.gatling.core.Predef.defaultJsonParsers, io.gatling.core.Predef.configuration),
              )
          case KafkaCheckType.ResponseCode =>
            scalaCheck
              .asInstanceOf[CheckBuilder[checks.KafkaCheckMaterializer.KafkaMessageCheckType, KafkaProtocolMessage]]
              .build(
                KafkaCheckMaterializer.kafkaStatusCheck,
              )
          case unknown => throw new IllegalArgumentException(s"Kafka DSL doesn't support $unknown")
        }
    }
  }

  def toScalaChecks(javaChecks: ju.List[Object]): Seq[KafkaCheck] =
    javaChecks.asScala.map(toScalaCheck).toSeq
}
