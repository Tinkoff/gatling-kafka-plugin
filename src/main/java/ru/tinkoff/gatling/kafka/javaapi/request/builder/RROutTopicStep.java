package ru.tinkoff.gatling.kafka.javaapi.request.builder;

import static io.gatling.javaapi.core.internal.Expressions.*;

public class RROutTopicStep {

    private String inputTopic;
    private String outputTopic;

    private ru.tinkoff.gatling.kafka.actions.KafkaRequestReplyActionBuilder wrapped;

    public RROutTopicStep(String inputTopic, String outputTopic) {
        return ;
    }
}
