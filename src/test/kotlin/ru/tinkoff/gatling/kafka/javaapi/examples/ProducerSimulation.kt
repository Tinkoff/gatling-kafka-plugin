package ru.tinkoff.gatling.kafka.javaapi.examples

import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.Simulation
import org.apache.kafka.clients.producer.ProducerConfig
import ru.tinkoff.gatling.kafka.javaapi.KafkaDsl.*
import java.time.Duration

class ProducerSimulation : Simulation() {

    private val kafkaConsumerConf = kafka().topic("test.topic")
        .properties(mapOf<String, Any>(ProducerConfig.ACKS_CONFIG to "1",
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer",
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer"
        ))

    private val scn = scenario("Basic")
        .exec(kafka("BasicRequest").send("foo"))
        .exec(kafka("dld").send("true", "12.0"))

    init {
        setUp(
            scn.injectOpen(atOnceUsers(1))
        )
            .protocols(kafkaConsumerConf)
            .maxDuration(Duration.ofSeconds(120))
    }

}