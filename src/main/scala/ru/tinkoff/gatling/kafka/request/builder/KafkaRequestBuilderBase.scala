package ru.tinkoff.gatling.kafka.request.builder

import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session.Expression
import org.apache.kafka.common.header.Headers
import org.apache.kafka.common.serialization.Serde
import ru.tinkoff.gatling.kafka.KafkaCheck
import ru.tinkoff.gatling.kafka.actions.KafkaRequestReplyActionBuilder

import scala.reflect.ClassTag

case class KafkaRequestBuilderBase(requestName: Expression[String]) {

  def send[K, V](key: Expression[K], payload: Expression[V], headers: Expression[Headers])(implicit
      sender: Sender[K, V],
  ): RequestBuilder[K, V] =
    sender.send(requestName, Some(key), payload, Some(headers))

  def send[K, V](key: Expression[K], payload: Expression[V])(implicit sender: Sender[K, V]): RequestBuilder[K, V] =
    sender.send(requestName, Some(key), payload)

  def send[V](payload: Expression[V])(implicit sender: Sender[Nothing, V]): RequestBuilder[_, V] =
    sender.send(requestName, None, payload)

  def requestReply: ReqRepBase.type = ReqRepBase

  object ReqRepBase {
    case class RROutTopicStep(inTopic: Expression[String], outTopic: Expression[String]) {
      def send[K: Serde: ClassTag, V: Serde: ClassTag](
          key: Expression[K],
          payload: Expression[V],
          headers: Expression[Headers],
      ): KafkaRequestReplyActionBuilder[K, V] = {
        KafkaRequestReplyActionBuilder[K, V](
          new KafkaRequestReplyAttributes[K, V](
            requestName,
            inTopic,
            outTopic,
            key,
            payload,
            Some(headers),
            implicitly[Serde[K]].serializer(),
            implicitly[Serde[V]].serializer(),
            List.empty,
          ),
        )
      }

      def send[K: Serde: ClassTag, V: Serde: ClassTag](
          key: Expression[K],
          payload: Expression[V],
      ): KafkaRequestReplyActionBuilder[K, V] =
        KafkaRequestReplyActionBuilder[K, V](
          new KafkaRequestReplyAttributes[K, V](
            requestName,
            inTopic,
            outTopic,
            key,
            payload,
            None,
            implicitly[Serde[K]].serializer(),
            implicitly[Serde[V]].serializer(),
            List.empty,
          ),
        )
    }
    case class RRInTopicStep(inTopic: Expression[String])                                {
      def replyTopic(outTopic: Expression[String]): RROutTopicStep = RROutTopicStep(inTopic, outTopic)
    }
    def requestTopic(rt: Expression[String]): RRInTopicStep = RRInTopicStep(rt)

  }

}
