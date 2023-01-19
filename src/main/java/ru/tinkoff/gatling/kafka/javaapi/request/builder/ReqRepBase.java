package ru.tinkoff.gatling.kafka.javaapi.request.builder;

public class ReqRepBase {
    public RRInTopicStep requestTopic(String inputTopic) {
        return new RRInTopicStep(inputTopic);
    }
}
