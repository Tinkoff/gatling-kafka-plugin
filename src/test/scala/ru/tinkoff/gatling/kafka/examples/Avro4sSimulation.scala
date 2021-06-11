package ru.tinkoff.gatling.kafka.examples

import com.sksamuel.avro4s._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import org.apache.kafka.clients.producer.ProducerConfig
import ru.tinkoff.gatling.kafka.Predef._
import ru.tinkoff.gatling.kafka.protocol.KafkaProtocol

import scala.concurrent.duration._

class Avro4sSimulation extends Simulation {

  val kafkaConf: KafkaProtocol = kafka
    // Kafka topic name
    .topic("test")
    // Kafka producer configs
    .properties(
      Map(
        ProducerConfig.ACKS_CONFIG                   -> "1",
        // list of Kafka broker hostname and port pairs
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG      -> "localhost:9092",
        // in most cases, StringSerializer or ByteArraySerializer
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG   ->
          "org.apache.kafka.common.serialization.StringSerializer",
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG ->
          "org.apache.kafka.common.serialization.StringSerializer"
      )
    )

  val kafkaAclConf: KafkaProtocol = kafka
    .topic("my.acl.topic")
    .properties(
      Map(
        ProducerConfig.ACKS_CONFIG                   -> "1",
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG      -> "localhost:9092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG   -> "org.apache.kafka.common.serialization.StringSerializer",
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG -> "io.confluent.kafka.serializers.KafkaAvroSerializer",
        "value.subject.name.strategy"                -> "io.confluent.kafka.serializers.subject.RecordNameStrategy",
        "schema.registry.url"                        -> "http://schema.registry.com",
        "security.protocol"                          -> "SASL_PLAINTEXT",
        "sasl.mechanism"                             -> "SCRAM-SHA-512",
        "sasl.jaas.config"                           -> s"""org.apache.kafka.common.security.scram.ScramLoginModule required username="MY-USER" password="SECRET-PASSWORD";"""
      )
    )

  case class Ingredient(name: String, sugar: Double, fat: Double)

  implicit lazy val ingridientToRecord: ToRecord[Ingredient]     = ToRecord.apply
  implicit lazy val ingridientFromRecord: FromRecord[Ingredient] = FromRecord.apply
  implicit lazy val ingridientScemaFor: SchemaFor[Ingredient]    = SchemaFor.apply
  implicit lazy val ingridientFormat: RecordFormat[Ingredient]   = RecordFormat.apply

  val scn: ScenarioBuilder = scenario("Kafka Test")
    .exec(
      kafka("Simple Request")
        // message to send
        .send[Ingredient](Ingredient("Cheese", 0d, 70d))
    )
    .exec(
      kafka("Simple Request with Key")
        // message to send
        .send[String, Ingredient]("Key", Ingredient("Cheese", 0d, 70d))
    )

  setUp(
    scn
      .inject(constantUsersPerSec(10) during (90 seconds))
  )
    .protocols(kafkaConf)
}
