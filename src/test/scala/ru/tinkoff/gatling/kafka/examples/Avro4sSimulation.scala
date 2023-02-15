package ru.tinkoff.gatling.kafka.examples

import com.sksamuel.avro4s._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.header.Headers
import org.apache.kafka.common.header.internals.RecordHeaders
import ru.tinkoff.gatling.kafka.Predef._
import ru.tinkoff.gatling.kafka.protocol.KafkaProtocol

class Avro4sSimulation extends Simulation {

  val kafkaAclConf: KafkaProtocol = kafka
    .topic("my.acl.topic")
    .properties(
      Map(
        ProducerConfig.ACKS_CONFIG                   -> "1",
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG      -> "localhost:9092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG   -> "org.apache.kafka.common.serialization.StringSerializer",
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG -> "io.confluent.kafka.serializers.KafkaAvroSerializer",
        "value.subject.name.strategy"                -> "io.confluent.kafka.serializers.subject.RecordNameStrategy",
        "schema.registry.url"                        -> "http://localhost:9094",
      ),
    )

  case class Ingredient(name: String, sugar: Double, fat: Double)

  implicit val ingridientToRecord: ToRecord[Ingredient]     = ToRecord.apply
  implicit val ingridientFromRecord: FromRecord[Ingredient] = FromRecord.apply
  implicit val ingridientSchemaFor: SchemaFor[Ingredient]   = SchemaFor.apply
  implicit val ingridientFormat: RecordFormat[Ingredient]   = RecordFormat.apply
  implicit val ingredientHeaders: Headers                   = new RecordHeaders()

  val scn: ScenarioBuilder = scenario("Kafka Test")
    .exec(
      kafka("Simple Avro4s Request")
        // message to send
        .send[Ingredient](Ingredient("Cheese", 0d, 70d)),
    )
    .exec(
      kafka("Simple Avro4s Request with Key")
        // message to send
        .send[String, Ingredient]("Key", Ingredient("Cheese", 0d, 70d)),
    )

  setUp(scn.inject(atOnceUsers(1))).protocols(kafkaAclConf)
}
