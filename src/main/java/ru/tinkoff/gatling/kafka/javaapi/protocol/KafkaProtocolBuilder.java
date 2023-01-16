package ru.tinkoff.gatling.kafka.javaapi.protocol;

import io.gatling.core.protocol.Protocol;
import io.gatling.javaapi.core.ProtocolBuilder;

import java.util.Collections;
import java.util.Map;
import static scala.jdk.javaapi.CollectionConverters.asScala;

public class KafkaProtocolBuilder implements ProtocolBuilder {

    private ru.tinkoff.gatling.kafka.protocol.KafkaProtocolBuilder wrapped;

    public KafkaProtocolBuilder(ru.tinkoff.gatling.kafka.protocol.KafkaProtocolBuilder wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Protocol protocol() {
        return wrapped.build();
    }

}
