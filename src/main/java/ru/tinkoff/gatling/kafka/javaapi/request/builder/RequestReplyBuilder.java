package ru.tinkoff.gatling.kafka.javaapi.request.builder;

import io.gatling.javaapi.core.ActionBuilder;

public class RequestReplyBuilder<K, V> implements ActionBuilder {

    private final ru.tinkoff.gatling.kafka.actions.KafkaRequestReplyActionBuilder<K, V> wrapped;

    public RequestReplyBuilder(ru.tinkoff.gatling.kafka.actions.KafkaRequestReplyActionBuilder<K,V> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public io.gatling.core.action.builder.ActionBuilder asScala() {
        return wrapped;
    }
}