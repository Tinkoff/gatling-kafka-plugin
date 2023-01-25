package ru.tinkoff.gatling.javaapi.kafka.examples;

import io.gatling.javaapi.core.Simulation;
import org.apache.kafka.common.header.internals.RecordHeaders;
import ru.tinkoff.gatling.kafka.javaapi.protocol.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import ru.tinkoff.gatling.kafka.javaapi.request.builder.RequestReplyBuilder;

import java.time.Duration;
import java.util.Map;

import static ru.tinkoff.gatling.kafka.javaapi.KafkaDsl.kafka;

public class BasicSimulation extends Simulation {

    // Имеется KafkaProtocolBuilderBase, он делится на не rr и requestReply
    // не requestReply
    public KafkaProtocolBuilder k1 = kafka().topic("").properties(Map.of(ProducerConfig.ACKS_CONFIG, "1"));
    // requestReply
    public KafkaProtocolBuilderNew r1 = kafka()
            .requestReply()
            .producerSettings(
                    Map.of(
                            ProducerConfig.ACKS_CONFIG, "1",
                            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"
                    )
            )
            .consumeSettings(
                    Map.of(
                            "bootstrap.servers", "localhost:9092"
                    )
            )
            .withDefaultTimeout()
            .matchByValue();

    public KafkaProtocolBuilderNew r2 = kafka()
            .requestReply()
            .producerSettings(
                    Map.of(
                            ProducerConfig.ACKS_CONFIG, "1",
                            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"
                    )
            )
            .consumeSettings(
                    Map.of(
                            "bootstrap.servers", "localhost:9092"
                    )
            )
            .timeout(Duration.ofSeconds(30));

    public RequestReplyBuilder<?, ?> krr1  = kafka("requestReply")
            .requestReply()
            .requestTopic("inputTopic")
            .replyTopic("outputTopic")
            .send("somekey", "someval", new RecordHeaders(), String.class, String.class);
}
