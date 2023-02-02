package ru.tinkoff.gatling.kafka.javaapi.request.builder;

import io.gatling.javaapi.core.ActionBuilder;

public class RequestBuilder<K, V> implements ActionBuilder {

    private final ru.tinkoff.gatling.kafka.request.builder.RequestBuilder<K, V> wrapped;

    public RequestBuilder(ru.tinkoff.gatling.kafka.request.builder.RequestBuilder<K,V> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public io.gatling.core.action.builder.ActionBuilder asScala() {
        return wrapped.build();
    }
}
