package ru.tinkoff.gatling.kafka.javaapi.request.builder;

public class ReqRepBase {

    private final String requestName;

    public ReqRepBase(String requestName) {
        this.requestName = requestName;
    }

    public RRInTopicStep requestTopic(String inputTopic) {
        return new RRInTopicStep(inputTopic, this.requestName);
    }
}
