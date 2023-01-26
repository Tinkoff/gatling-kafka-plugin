package ru.tinkoff.gatling.kafka.javaapi.request.builder;

public class RRInTopicStep {

    private final String inputTopic;
    private final String requestName;

    public RRInTopicStep(String inputTopic, String requestName) {
        this.inputTopic = inputTopic;
        this.requestName = requestName;
    }

    public RROutTopicStep replyTopic(String outputTopic) {
        return new RROutTopicStep(this.inputTopic, outputTopic, this.requestName);
    }
}
