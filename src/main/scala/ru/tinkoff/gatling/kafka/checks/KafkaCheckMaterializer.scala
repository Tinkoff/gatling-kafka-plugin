package ru.tinkoff.gatling.kafka.checks

import com.fasterxml.jackson.databind.JsonNode
import io.gatling.commons.validation.SuccessWrapper
import io.gatling.core.check.bytes.BodyBytesCheckType
import io.gatling.core.check.jmespath.JmesPathCheckType
import io.gatling.core.check.jsonpath.JsonPathCheckType
import io.gatling.core.check.string.BodyStringCheckType
import io.gatling.core.check.substring.SubstringCheckType
import io.gatling.core.check.xpath.XPathCheckType
import io.gatling.core.check.{CheckMaterializer, Preparer}
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.json.JsonParsers
import net.sf.saxon.s9api.XdmNode
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.common.serialization.Serde
import ru.tinkoff.gatling.kafka.KafkaCheck
import ru.tinkoff.gatling.kafka.request.KafkaProtocolMessage

final class KafkaCheckMaterializer[T, P] private[KafkaCheckMaterializer] (
    override val preparer: Preparer[KafkaProtocolMessage, P],
) extends CheckMaterializer[T, KafkaCheck, KafkaProtocolMessage, P](identity)

object KafkaCheckMaterializer {
  def xpath(configuration: GatlingConfiguration): KafkaCheckMaterializer[XPathCheckType, XdmNode] =
    new KafkaCheckMaterializer(KafkaMessagePreparer.xmlPreparer(configuration))

  val bodyBytes: KafkaCheckMaterializer[BodyBytesCheckType, Array[Byte]] =
    new KafkaCheckMaterializer(KafkaMessagePreparer.bytesBodyPreparer)

  def bodyString(configuration: GatlingConfiguration): KafkaCheckMaterializer[BodyStringCheckType, String] =
    new KafkaCheckMaterializer(KafkaMessagePreparer.stringBodyPreparer(configuration))

  def substring(configuration: GatlingConfiguration): KafkaCheckMaterializer[SubstringCheckType, String] =
    new KafkaCheckMaterializer(KafkaMessagePreparer.stringBodyPreparer(configuration))

  def jsonPath(
      jsonParsers: JsonParsers,
      configuration: GatlingConfiguration,
  ): KafkaCheckMaterializer[JsonPathCheckType, JsonNode] =
    new KafkaCheckMaterializer(KafkaMessagePreparer.jsonPathPreparer(jsonParsers, configuration))

  def jmesPath(
      jsonParsers: JsonParsers,
      configuration: GatlingConfiguration,
  ): KafkaCheckMaterializer[JmesPathCheckType, JsonNode] =
    new KafkaCheckMaterializer(KafkaMessagePreparer.jsonPathPreparer(jsonParsers, configuration))

  type KafkaMessageCheckType

  val kafkaStatusCheck: KafkaCheckMaterializer[KafkaMessageCheckType, KafkaProtocolMessage] =
    new KafkaCheckMaterializer(_.success)

  def avroBody[T <: GenericRecord: Serde](
      configuration: GatlingConfiguration,
      topic: String,
  ): KafkaCheckMaterializer[KafkaMessageCheckType, T] =
    new KafkaCheckMaterializer(KafkaMessagePreparer.avroPreparer[T](configuration, topic))
}
