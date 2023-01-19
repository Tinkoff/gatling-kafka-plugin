package ru.tinkoff.gatling.kafka.javaapi;

import static io.gatling.javaapi.core.internal.Expressions.*;

import ru.tinkoff.gatling.kafka.javaapi.protocol.*;
import ru.tinkoff.gatling.kafka.javaapi.request.builder.*;

public final class KafkaDsl {

    public static KafkaProtocolBuilderBase kafka() {
        return new KafkaProtocolBuilderBase();
    }

    public static KafkaRequestBuilderBase kafka(String requestName) {
        return new KafkaRequestBuilderBase(ru.tinkoff.gatling.kafka.Predef.kafka(toStringExpression(requestName)));
    }

}
