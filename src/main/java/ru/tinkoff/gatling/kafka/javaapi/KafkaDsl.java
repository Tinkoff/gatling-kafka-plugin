package ru.tinkoff.gatling.kafka.javaapi;

import static io.gatling.javaapi.core.internal.Expressions.*;

import ru.tinkoff.gatling.kafka.javaapi.protocol.*;
import ru.tinkoff.gatling.kafka.request.builder.*;

public final class KafkaDsl {

    public static KafkaProtocolBuilderBase kafka() {
        return new KafkaProtocolBuilderBase();
    }

}
