package ru.tinkoff.gatling.kafka.javaapi.protocol;

import io.gatling.core.protocol.Protocol;
import io.gatling.javaapi.core.ProtocolBuilder;
import ru.tinkoff.gatling.kafka.request.KafkaProtocolMessage;
import scala.Function1;

public class KafkaProtocolBuilderNew implements ProtocolBuilder {

    private ru.tinkoff.gatling.kafka.protocol.KafkaProtocolBuilderNew wrapped;

    public KafkaProtocolBuilderNew(ru.tinkoff.gatling.kafka.protocol.KafkaProtocolBuilderNew wrapped) {
        this.wrapped = wrapped;
    }

    public KafkaProtocolBuilderNew matchByValue() {
        this.wrapped = wrapped.matchByValue();
        return this;
    }

    public KafkaProtocolBuilderNew matchByMessage(Function1<KafkaProtocolMessage, byte[]> keyExtractor) {
        this.wrapped = wrapped.matchByMessage(keyExtractor);
        return this;
    }

    public KafkaProtocolBuilderNew skipMatches( int n) {
        this.wrapped = wrapped.skipMatches(n);
        return this;
    }

    @Override
    public Protocol protocol() {
        return wrapped.build();
    }

}


