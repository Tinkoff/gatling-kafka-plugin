package ru.tinkoff.gatling.kafka.javaapi.examples

import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.Simulation
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.header.internals.RecordHeaders
import ru.tinkoff.gatling.kafka.javaapi.KafkaDsl.*
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

class BasicSimulation : Simulation() {

    private val kafkaConf = kafka()
        .topic("test.topic")
        .properties(mapOf<String, Any>(ProducerConfig.ACKS_CONFIG to "1"))

    private val kafkaProtocolC = kafka().requestReply()
        .producerSettings(
            mapOf<String, Any>(
                ProducerConfig.ACKS_CONFIG to "1",
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            )
        )
        .consumeSettings(
            mapOf<String, Any>("bootstrap.servers" to "localhost:9092")
        ).timeout(Duration.ofSeconds(5))

    private val c = AtomicInteger(0)

    private val feeder = generateSequence {
        mapOf("kekey" to c.incrementAndGet())
    }.iterator()

    private val headers = RecordHeaders().add("test-header", "test_value".toByteArray())

    private val scn = scenario("Basic")
        .feed(feeder)
        .exec(
            kafka("ReqRep").requestReply()
                .requestTopic("test.t")
                .replyTopic("test.t")
                .send(
                    "#{kekey}",
                    """{ "m": "dkf" }""",
                    headers,
                    String::class.java,
                    String::class.java
                )
                .check(jsonPath("$.m").`is`("dkf"))
        )

        init {
            setUp(scn.injectOpen(atOnceUsers(5))).protocols(kafkaProtocolC).maxDuration(Duration.ofSeconds(120))
        }
}