package ru.tinkoff.gatling.kafka.javaapi.protocol;

import java.util.Collections;

public class KafkaProtocolBuilderBase {

    public KafkaProtocolBuilderPropertiesStep topic(String name) {
        return new KafkaProtocolBuilderPropertiesStep(name, Collections.emptyMap());
    }

    public KafkaProtocolBuilderNewBase requestReply() {
        return new KafkaProtocolBuilderNewBase();
    }

}
