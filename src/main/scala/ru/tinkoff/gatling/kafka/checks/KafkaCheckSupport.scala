package ru.tinkoff.gatling.kafka.checks

import com.fasterxml.jackson.databind.JsonNode
import io.gatling.core.check.Check.PreparedCache
import io.gatling.core.check.{Check, CheckBuilder, CheckMaterializer, CheckResult, TypedCheckIfMaker, UntypedCheckIfMaker}
import io.gatling.core.check.bytes.BodyBytesCheckType
import io.gatling.core.check.jmespath.JmesPathCheckType
import io.gatling.core.check.jsonpath.JsonPathCheckType
import io.gatling.core.check.string.BodyStringCheckType
import io.gatling.core.check.substring.SubstringCheckType
import io.gatling.core.check.xpath.XPathCheckType
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.json.JsonParsers
import io.gatling.core.session.Session
import net.sf.saxon.s9api.XdmNode
import ru.tinkoff.gatling.kafka.KafkaCheck
import ru.tinkoff.gatling.kafka.request.KafkaProtocolMessage
import io.gatling.commons.validation._
import ru.tinkoff.gatling.kafka.checks.KafkaCheckMaterializer.KafkaMessageCheckType

import scala.annotation.implicitNotFound

trait KafkaCheckSupport {
  def messageCheck: KafkaMessageCheck.type = KafkaMessageCheck

  def simpleCheck(f: KafkaProtocolMessage => Boolean): KafkaCheck =
    Check.Simple(
      (response: KafkaProtocolMessage, _: Session, _: PreparedCache) =>
        if (f(response)) {
          CheckResult.NoopCheckResultSuccess
        } else {
          "Kafka record check failed".failure
        },
      None,
    )

  @implicitNotFound("Could not find a CheckMaterializer. This check might not be valid for Kafka.")
  implicit def checkBuilder2KafkaCheck[T, P](
      checkBuilder: CheckBuilder[T, P],
  )(implicit materializer: CheckMaterializer[T, KafkaCheck, KafkaProtocolMessage, P]): KafkaCheck =
    checkBuilder.build(materializer)

  @implicitNotFound("Could not find a CheckMaterializer. This check might not be valid for Kafka.")
  implicit def validatorCheckBuilder2KafkaCheck[T, P, X](
      validate: CheckBuilder.Validate[T, P, X],
  )(implicit materializer: CheckMaterializer[T, KafkaCheck, KafkaProtocolMessage, P]): KafkaCheck =
    validate.exists

  @implicitNotFound("Could not find a CheckMaterializer. This check might not be valid for Kafka.")
  implicit def findCheckBuilder2KafkaCheck[T, P, X](
      find: CheckBuilder.Find[T, P, X],
  )(implicit materializer: CheckMaterializer[T, KafkaCheck, KafkaProtocolMessage, P]): KafkaCheck =
    find.find.exists

  implicit def kafkaXPathMaterializer(implicit
      configuration: GatlingConfiguration,
  ): KafkaCheckMaterializer[XPathCheckType, XdmNode] =
    KafkaCheckMaterializer.xpath(configuration)

  implicit def kafkaJsonPathMaterializer(implicit
      jsonParsers: JsonParsers,
      configuration: GatlingConfiguration,
  ): KafkaCheckMaterializer[JsonPathCheckType, JsonNode] =
    KafkaCheckMaterializer.jsonPath(jsonParsers, configuration)

  implicit def kafkaJmesPathMaterializer(implicit
      jsonParsers: JsonParsers,
      configuration: GatlingConfiguration,
  ): KafkaCheckMaterializer[JmesPathCheckType, JsonNode] =
    KafkaCheckMaterializer.jmesPath(jsonParsers, configuration)

  implicit def kafkaBodyStringMaterializer(implicit
      configuration: GatlingConfiguration,
  ): KafkaCheckMaterializer[BodyStringCheckType, String] =
    KafkaCheckMaterializer.bodyString(configuration)

  implicit def kafkaSubstringMaterializer(implicit
      configuration: GatlingConfiguration,
  ): KafkaCheckMaterializer[SubstringCheckType, String] =
    KafkaCheckMaterializer.substring(configuration)

  implicit def kafkaBodyByteMaterializer: KafkaCheckMaterializer[BodyBytesCheckType, Array[Byte]] =
    KafkaCheckMaterializer.bodyBytes

  implicit val kafkaStatusCheckMaterializer: KafkaCheckMaterializer[KafkaMessageCheckType, KafkaProtocolMessage] =
    KafkaCheckMaterializer.kafkaStatusCheck

  implicit val kafkaUntypedConditionalCheckWrapper: UntypedCheckIfMaker[KafkaCheck] = _.checkIf(_)

  implicit val kafkaTypedConditionalCheckWrapper: TypedCheckIfMaker[KafkaProtocolMessage, KafkaCheck] = _.checkIf(_)
}
