package ru.tinkoff.gatling.javaapi.kafka.examples;

import io.gatling.javaapi.core.Simulation;
import ru.tinkoff.gatling.kafka.javaapi.protocol.*;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Map;

import static ru.tinkoff.gatling.kafka.javaapi.KafkaDsl.kafka;

public class BasicSimulation extends Simulation {

    // Имеется KafkaProtocolBuilderBase, он делится на не rr и requestReply
    // не requestReply
    public KafkaProtocolBuilderBase k = kafka();
    public KafkaProtocolBuilderPropertiesStep k1 = kafka().topic("myTopic");
    // у не requestReply есть properties(), возвращает KafkaProtocolBuilder
    public KafkaProtocolBuilder k2 = kafka().topic("").properties(Map.of(ProducerConfig.ACKS_CONFIG, "1"));
    // requestReply
    public KafkaProtocolBuilderNewBase r = kafka().requestReply();

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

}
