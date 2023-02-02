package ru.tinkoff.gatling.kafka.javaapi.examples;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import org.apache.kafka.clients.producer.ProducerConfig;
import ru.tinkoff.gatling.kafka.javaapi.protocol.KafkaProtocolBuilder;

import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static ru.tinkoff.gatling.kafka.javaapi.KafkaDsl.kafka;

public class ProducerSimulation extends Simulation {

    private final KafkaProtocolBuilder kafkaConsumerConf =
            kafka().topic("test.topic")
            .properties(Map.of(ProducerConfig.ACKS_CONFIG, "1"));

    private final ScenarioBuilder scn = scenario("Basic")
            .exec(kafka("BasicRequest").send("foo"))
            .exec(kafka("dld").send("true", 12.0));

}
