package ru.tinkoff.gatling.kafka.javaapi.protocol;

import java.util.Map;

public class KPProducerSettingsStep {

    private final Map<String, Object> ps;

    public KPProducerSettingsStep(Map<String, Object> ps) {
        this.ps = ps;
    }

    public KPConsumeSettingsStep consumeSettings(Map<String, Object> cs) {
        return new KPConsumeSettingsStep(ps, cs);
    }
}
