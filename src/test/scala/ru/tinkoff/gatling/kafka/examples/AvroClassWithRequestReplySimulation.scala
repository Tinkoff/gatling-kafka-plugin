package ru.tinkoff.gatling.kafka.examples

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient
import io.confluent.kafka.serializers.{KafkaAvroDeserializer, KafkaAvroSerializer}
import io.gatling.core.Predef._
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}
import ru.tinkoff.gatling.kafka.Predef._
import ru.tinkoff.gatling.kafka.actions.KafkaRequestReplyActionBuilder
import ru.tinkoff.gatling.kafka.protocol.KafkaProtocol

import scala.concurrent.duration.DurationInt
import scala.jdk.CollectionConverters._

class AvroClassWithRequestReplySimulation extends Simulation {

  // default serde for unknown classes is avro serde
  // schemaRegUrl must be specified if custom avro scheme is used, when the send method requires implicit
  implicit val schemaRegUrl: String = "http://localhost:9094"

  // example if you want to use your own or custom serde
  val ser =
    new KafkaAvroSerializer(
      new CachedSchemaRegistryClient("schRegUrl".split(',').toList.asJava, 16),
    )

  val de =
    new KafkaAvroDeserializer(
      new CachedSchemaRegistryClient("schRegUrl".split(',').toList.asJava, 16),
    )

  implicit val serdeClass: Serde[AvroClass] = new Serde[AvroClass] {
    override def serializer(): Serializer[AvroClass] = ser.asInstanceOf[Serializer[AvroClass]]

    override def deserializer(): Deserializer[AvroClass] = de.asInstanceOf[Deserializer[AvroClass]]
  }

  // protocol
  val kafkaProtocolRRAvro: KafkaProtocol = kafka.requestReply
    .producerSettings(
      Map(
        ProducerConfig.ACKS_CONFIG -> "1",
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9093",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringSerializer",
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG -> "io.confluent.kafka.serializers.KafkaAvroSerializer",
        // schema registry url is required for KafkaAvroSerializer and KafkaAvroDeserializer
        "schema.registry.url" -> "http://localhost:9094",
      ),
    )
    .consumeSettings(
      Map(
        "bootstrap.servers" -> "localhost:9093",
      ),
    )
    .timeout(5.seconds)

  // message
  val kafkaMessage: KafkaRequestReplyActionBuilder[String, AvroClass] = kafka("RequestReply").requestReply
    .requestTopic("request.t")
    .replyTopic("reply.t")
    .send[String, AvroClass]("key", AvroClass())

  // simulation
  setUp(scenario("Kafka RequestReply Avro").exec(kafkaMessage).inject(atOnceUsers(1))).protocols(kafkaProtocolRRAvro)

  case class AvroClass()
}
