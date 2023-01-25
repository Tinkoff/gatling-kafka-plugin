package ru.tinkoff.gatling.kafka.javaapi.request.builder;

import org.apache.kafka.common.header.Headers;

import static io.gatling.javaapi.core.internal.Expressions.*;

public class KafkaRequestBuilderBase {

    private final ru.tinkoff.gatling.kafka.request.builder.KafkaRequestBuilderBase wrapped;

    public KafkaRequestBuilderBase(ru.tinkoff.gatling.kafka.request.builder.KafkaRequestBuilderBase wrapped){
        this.wrapped = wrapped;
    }

    public <K,V> RequestBuilder<?, ?> send(K key, V payload, Headers headers) {
        return new RequestBuilder<>(
                wrapped.send(
                        toStaticValueExpression(key),
                        toStaticValueExpression(payload),
                        toStaticValueExpression(headers),
                        ru.tinkoff.gatling.kafka.request.builder.Sender.noSchemaSender()
                ));
    }

    public <V> RequestBuilder<Object, ?> send(V payload) {
        return new RequestBuilder<>(wrapped.send(
                toStaticValueExpression(payload),
                ru.tinkoff.gatling.kafka.request.builder.Sender.noSchemaSender()));
    }

    public ReqRepBase requestReply() {
        return new ReqRepBase();
    }

}
