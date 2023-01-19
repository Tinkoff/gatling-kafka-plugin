package ru.tinkoff.gatling.kafka.javaapi.request.builder;

import ru.tinkoff.gatling.kafka.request.builder.RequestBuilder;
import org.apache.kafka.common.header.Headers;
import static io.gatling.javaapi.core.internal.Expressions.*;

import java.util.List;

public class KafkaRequestBuilderBase {

    private final ru.tinkoff.gatling.kafka.request.builder.KafkaRequestBuilderBase wrapped;

    public KafkaRequestBuilderBase(ru.tinkoff.gatling.kafka.request.builder.KafkaRequestBuilderBase wrapped){
        this.wrapped = wrapped;
    }

    public <K, V> RequestBuilder<K, V> send(K key, V payload, List<Headers> headers) {
        //toStaticValueExpression(key);
        return wrapped.send(
                toStaticValueExpression(key),
                toStaticValueExpression(payload),
                toExpression(headers.toString(), Headers.class),
                null);
    }

    public ReqRepBase requestReply() {
        return new ReqRepBase();
    }

}

