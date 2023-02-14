package ru.tinkoff.gatling.kafka.javaapi.examples

import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.Simulation
import org.apache.kafka.clients.producer.ProducerConfig
import ru.tinkoff.gatling.kafka.javaapi.*
import ru.tinkoff.gatling.kafka.javaapi.KafkaDsl.*
import ru.tinkoff.gatling.kafka.request.*
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

class MatchSimulation : Simulation() {

    private val kafkaProtocolMatchByValue = kafka().requestReply()
        .producerSettings(
            mapOf<String, Any>(
                ProducerConfig.ACKS_CONFIG to "1",
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092"
            )
        )
        .consumeSettings(
            mapOf<String, Any>("bootstrap.servers" to "localhost:9092")
        )
        .timeout(Duration.ofSeconds(5)) // for match by message value
        .matchByValue()

    private fun matchByOwnVal(message: KafkaProtocolMessage): ByteArray {
        // do something with the message and extract the values you are interested in
        // method is called:
        // - for each message which will be sent out
        // - for each message which has been received
        return "Custom Message".toByteArray() // just returning something
    }

    private val kafkaProtocolMatchByMessage = kafka().requestReply()
        .producerSettings(
            mapOf<String, Any>(
                ProducerConfig.ACKS_CONFIG to "1",
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092"
            )
        )
        .consumeSettings(
            mapOf<String, Any>(
                "bootstrap.servers" to "localhost:9092"
            )
        )
        .timeout(Duration.ofSeconds(5))
        .matchByMessage { message: KafkaProtocolMessage -> matchByOwnVal(message) }

    private val kafkaProtocolMatchByKeyExpectMultipleMessagesInResponse = kafka().requestReply()
        .producerSettings(
            mapOf<String, Any>(
                ProducerConfig.ACKS_CONFIG to "1",
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092"
            )
        )
        .consumeSettings(
            mapOf<String, Any>(
                "bootstrap.servers" to "localhost:9092"
            )
        )
        .timeout(Duration.ofSeconds(5))
        // each request message is matched by 3 replies, first two matched messages will be skipped and the third one recorded
        .skipMatches(2)

    private val c = AtomicInteger(0)

    private val feeder = generateSequence {
        mapOf("kekey" to c.incrementAndGet())
    }.iterator()

    private val scn = scenario("Basic")
        .feed(feeder)
        .exec(
            KafkaDsl.kafka("ReqRep").requestReply()
                .requestTopic("test.t")
                .replyTopic("test.t")
                .send<String, String>(
                    "#{kekey}",
                    """{ "m": "dkf" }""",
                    String::class.java,
                    String::class.java
                )
        )

        init
        {
            setUp(
                scn.injectOpen(atOnceUsers(1))
            )
                .protocols(kafkaProtocolMatchByMessage)
                .maxDuration(Duration.ofSeconds(120))
        }

}