package ru.tinkoff.gatling.kafka.actions

import io.gatling.commons.stats.KO
import io.gatling.commons.util.Clock
import io.gatling.commons.validation._
import io.gatling.core.action.{Action, RequestAction}
import io.gatling.core.session.{Expression, Session}
import io.gatling.core.stats.StatsEngine
import io.gatling.core.util.NameGen
import org.apache.kafka.common.serialization.Serializer
import ru.tinkoff.gatling.kafka.KafkaLogging
import ru.tinkoff.gatling.kafka.protocol.KafkaComponents
import ru.tinkoff.gatling.kafka.request.KafkaProtocolMessage
import ru.tinkoff.gatling.kafka.request.builder.KafkaRequestReplyAttributes
import io.gatling.core.session.el._

import scala.reflect.{ClassTag, classTag}

class KafkaRequestReplyAction[K: ClassTag, V: ClassTag](
    components: KafkaComponents,
    val attr: KafkaRequestReplyAttributes[K, V],
    val statsEngine: StatsEngine,
    val clock: Clock,
    val next: Action,
) extends RequestAction with KafkaLogging with NameGen {
  override def requestName: Expression[String] = attr.requestName

  override def sendRequest(session: Session): Validation[Unit] = {
    val now = clock.nowMillis
    for {
      rn    <- requestName(session)
      topic <- attr.outTopic(session)
      msg   <- resolveToProtocolMessage(session)
    } yield components.sender.send(topic, msg)(
      rm => {
        if (logger.underlying.isDebugEnabled) {
          logMessage(s"Record sent user=${session.userId} key=${new String(msg.key)} topic=${rm.topic()}", msg)
        }
        val id = msg.key
        components.trackersPool
          .tracker(topic, None)
          .track(id, clock.nowMillis, components.kafkaProtocol.timeout.toMillis, attr.checks, session, next, rn)
      },
      e => {
        logger.error(e.getMessage, e)
        statsEngine.logResponse(
          session.scenario,
          session.groups,
          rn,
          now,
          clock.nowMillis,
          KO,
          Some("500"),
          Some(e.getMessage),
        )
      },
    )
  }

  private def serializeKey(
      serializer: Serializer[K],
      keyE: Expression[K],
      topicE: Expression[String],
  ): Expression[Array[Byte]] = s =>
    // need for work gatling Expression Language
    if (classTag[K].runtimeClass.getCanonicalName == "java.lang.String")
      for {
        topic <- topicE(s)
        key   <- keyE.asInstanceOf[Expression[String]](s).flatMap(_.el[String].apply(s))
      } yield serializer.asInstanceOf[Serializer[String]].serialize(topic, key)
    else
      for {
        topic <- topicE(s)
        key   <- keyE(s)
      } yield serializer.serialize(topic, key)

  private def optToVal[T](ovt: Option[Validation[T]]): Validation[Option[T]] =
    ovt.fold(Option.empty[T].success)(_.map(Option[T]))

  private def resolveToProtocolMessage: Expression[KafkaProtocolMessage] = s =>
    // need for work gatling Expression Language
    if (classTag[V].runtimeClass.getCanonicalName == "java.lang.String")
      for {
        key     <- serializeKey(attr.keySerializer, attr.key, attr.inTopic)(s)
        topic   <- attr.inTopic(s)
        value   <- attr.value
                     .asInstanceOf[Expression[String]](s)
                     .flatMap(_.el[String].apply(s))
                     .map(v => attr.valueSerializer.asInstanceOf[Serializer[String]].serialize(topic, v))
        headers <- optToVal(attr.headers.map(_(s)))
      } yield KafkaProtocolMessage(key, value, headers)
    else
      for {
        key     <- serializeKey(attr.keySerializer, attr.key, attr.inTopic)(s)
        topic   <- attr.inTopic(s)
        value   <- attr.value(s).map(v => attr.valueSerializer.serialize(topic, v))
        headers <- optToVal(attr.headers.map(_(s)))
      } yield KafkaProtocolMessage(key, value, headers)

  override def name: String = genName("kafkaRequestReply")
}
