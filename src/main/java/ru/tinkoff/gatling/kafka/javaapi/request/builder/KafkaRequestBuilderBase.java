package ru.tinkoff.gatling.kafka.javaapi.request.builder;

import io.gatling.commons.validation.Validation;
import io.gatling.core.session.Session;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import scala.Function1;

import static io.gatling.javaapi.core.internal.Expressions.*;
import static io.gatling.javaapi.core.internal.Expressions.toStaticValueExpression;

public class KafkaRequestBuilderBase {

    private final ru.tinkoff.gatling.kafka.request.builder.KafkaRequestBuilderBase wrapped;
    private final String requestName;

    private <T> Function1<Session, Validation<T>> calculateExpression(T obj) {
        Function1<Session, Validation<T>> expression;

        if (obj instanceof String || obj.getClass().isPrimitive() || obj instanceof CharSequence || obj instanceof byte[]) {
            expression = toExpression(obj.toString(), obj.getClass());
        } else {
            expression = toStaticValueExpression(obj);
        }
        return expression;
    }

    public KafkaRequestBuilderBase(ru.tinkoff.gatling.kafka.request.builder.KafkaRequestBuilderBase wrapped, String requestName) {
        this.wrapped = wrapped;
        this.requestName = requestName;
    }

    public <K, V> RequestBuilder<?, ?> send(K key, V payload) {
        return new RequestBuilder<>(
                wrapped.send(
                        calculateExpression(key),
                        calculateExpression(payload),
                        toStaticValueExpression(new RecordHeaders()),
                        ru.tinkoff.gatling.kafka.request.builder.Sender.noSchemaSender()
                ));
    }

    public <K, V> RequestBuilder<?, ?> send(K key, V payload, Headers headers) {
        return new RequestBuilder<>(
                wrapped.send(
                        calculateExpression(key),
                        calculateExpression(payload),
                        toStaticValueExpression(headers),
                        ru.tinkoff.gatling.kafka.request.builder.Sender.noSchemaSender()
                ));
    }

    public <V> RequestBuilder<?, ?> send(V payload) {
        return new RequestBuilder<>(wrapped.send(
                calculateExpression(payload),
                ru.tinkoff.gatling.kafka.request.builder.Sender.noSchemaSender()));
    }

    public ReqRepBase requestReply() {
        return new ReqRepBase(requestName);
    }

}
