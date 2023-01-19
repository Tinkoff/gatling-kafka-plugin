package ru.tinkoff.gatling.kafka.javaapi.request.builder;

public class RRInTopicStep {

    private String inputTopic;

    public RRInTopicStep(String inputTopic) {
        this.inputTopic = inputTopic;
    }

    public RROutTopicStep replyTopic(String outputTopic) {
        return new RROutTopicStep(this.inputTopic, outputTopic);
    }
}
