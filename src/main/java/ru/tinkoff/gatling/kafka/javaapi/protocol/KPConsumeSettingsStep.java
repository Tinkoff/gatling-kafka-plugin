package ru.tinkoff.gatling.kafka.javaapi.protocol;

import java.time.Duration;
import java.util.Map;

import static scala.jdk.javaapi.CollectionConverters.asScala;
import scala.jdk.javaapi.DurationConverters;

public class KPConsumeSettingsStep {

    private final Map<String, Object> producerSettings;
    private final Map<String, Object> consumeSettings;

    public KPConsumeSettingsStep(Map<String, Object> producerSettings, Map<String, Object> consumeSettings) {
        this.producerSettings = producerSettings;
        this.consumeSettings = consumeSettings;
    }

    public KafkaProtocolBuilderNew timeout(Duration timeout) {
        scala.collection.immutable.Map<String, Object> ps = scala.collection.immutable.Map.from(asScala(this.producerSettings));
        scala.collection.immutable.Map<String, Object> cs = scala.collection.immutable.Map.from(asScala(this.consumeSettings));
        return new KafkaProtocolBuilderNew(ru.tinkoff.gatling.kafka.protocol.KafkaProtocolBuilderNew.apply(ps, cs, DurationConverters.toScala(timeout), ru.tinkoff.gatling.kafka.protocol.KafkaProtocol.KafkaKeyMatcher$.MODULE$,0));
    }

    public KafkaProtocolBuilderNew withDefaultTimeout() {
        scala.collection.immutable.Map<String, Object> ps = scala.collection.immutable.Map.from(asScala(this.producerSettings));
        scala.collection.immutable.Map<String, Object> cs = scala.collection.immutable.Map.from(asScala(this.consumeSettings));
        return new KafkaProtocolBuilderNew(ru.tinkoff.gatling.kafka.protocol.KafkaProtocolBuilderNew.apply(ps, cs, DurationConverters.toScala(Duration.ofSeconds(60)), ru.tinkoff.gatling.kafka.protocol.KafkaProtocol.KafkaKeyMatcher$.MODULE$,0));
    }
}
