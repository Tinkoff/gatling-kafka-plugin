package ru.tinkoff.gatling.kafka.javaapi.request.builder;

import org.apache.kafka.common.header.Headers;
import ru.tinkoff.gatling.kafka.request.builder.RequestBuilder;

import java.util.List;

public class RROutTopicStep {

    private String inputTopic;
    private String outputTopic;

    private ru.tinkoff.gatling.kafka.actions.KafkaRequestReplyActionBuilder wrapped;

    public RROutTopicStep(String inputTopic, String outputTopic) {
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;
    }

    public <K, V> RequestBuilder<K, V> send(K key, V payload, List<Headers> headers) {
        return null;
    }
}
