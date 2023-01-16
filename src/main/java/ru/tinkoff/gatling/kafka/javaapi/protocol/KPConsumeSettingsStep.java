package ru.tinkoff.gatling.kafka.javaapi.protocol;

import java.time.Duration;
import java.util.Map;

import static scala.jdk.javaapi.CollectionConverters.asScala;

import ru.tinkoff.gatling.kafka.protocol.KafkaProtocol;
import scala.concurrent.duration.FiniteDuration;
import scala.jdk.javaapi.DurationConverters;

public class KPConsumeSettingsStep {

    private Map<String, Object> producerSettings;
    private Map<String, Object> consumeSettings;

    private ru.tinkoff.gatling.kafka.protocol.KafkaProtocol protocol;

    public KPConsumeSettingsStep(Map<String, Object> producerSettings, Map<String, Object> consumeSettings) {
        this.producerSettings = producerSettings;
        this.consumeSettings = consumeSettings;
    }

    public KafkaProtocolBuilderNew timeout(FiniteDuration timeout) {
        scala.collection.immutable.Map<String, Object> ps = scala.collection.immutable.Map.from(asScala(this.producerSettings));
        scala.collection.immutable.Map<String, Object> cs = scala.collection.immutable.Map.from(asScala(this.consumeSettings));
        protocol.messageMatcher();
        return new KafkaProtocolBuilderNew(ru.tinkoff.gatling.kafka.protocol.KafkaProtocolBuilderNew.apply(ps, cs, timeout, protocol.messageMatcher()));
    }

    public KafkaProtocolBuilderNew withDefaultTimeout() {
        scala.collection.immutable.Map<String, Object> ps = scala.collection.immutable.Map.from(asScala(this.producerSettings));
        scala.collection.immutable.Map<String, Object> cs = scala.collection.immutable.Map.from(asScala(this.consumeSettings));
        return new KafkaProtocolBuilderNew(ru.tinkoff.gatling.kafka.protocol.KafkaProtocolBuilderNew.apply(ps, cs, DurationConverters.toScala(Duration.ofSeconds(5)), protocol.messageMatcher()));
    }
}
