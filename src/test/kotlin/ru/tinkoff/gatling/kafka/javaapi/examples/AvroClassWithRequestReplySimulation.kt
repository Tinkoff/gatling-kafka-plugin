package ru.tinkoff.gatling.kafka.javaapi.examples

import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.Simulation
import org.apache.kafka.clients.producer.ProducerConfig
import ru.tinkoff.gatling.kafka.javaapi.*
import ru.tinkoff.gatling.kafka.javaapi.KafkaDsl.*
import ru.tinkoff.gatling.kafka.request.*
import java.time.Duration

class AvroClassWithRequestReplySimulation : Simulation() {

    // example of using custom serde
    val ser = KafkaAvroSerializer(CachedSchemaRegistryClient("schRegUrl".split(','), 16),) as Serializer<MyAvroClass>
    val de = KafkaAvroDeserializer(CachedSchemaRegistryClient("schRegUrl".split(','), 16),) as Deserializer<MyAvroClass>

    // protocol
    val kafkaProtocolRRAvro = kafka()
        .requestReply()
        .producerSettings(
            mapOf<String, Any>(
                ProducerConfig.ACKS_CONFIG to "1",
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer",
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to "io.confluent.kafka.serializers.KafkaAvroSerializer",
                // schema registry url is required for KafkaAvroSerializer and KafkaAvroDeserializer
                "schema.registry.url" to "url"
            )
        )
        .consumeSettings(
            mapOf<String, Any>(
                "bootstrap.servers" to "localhost:9092"
            )
        )
        .timeout(Duration.ofSeconds(5))

    // message
    val kafkaMessage = kafka("RequestReply").requestReply()
        .requestTopic("request.t")
        .replyTopic("reply.t")
        .send("key", MyAvroClass(), String::class.java, MyAvroClass::class.java, ser, de)

    // simulation
    init {
        setUp(scenario("Kafka scenario").exec(kafkaMessage).injectOpen(atOnceUsers(1))).protocols(kafkaProtocolRRAvro)
    }

}