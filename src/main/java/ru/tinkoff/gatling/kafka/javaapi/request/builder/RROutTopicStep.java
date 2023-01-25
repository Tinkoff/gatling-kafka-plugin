package ru.tinkoff.gatling.kafka.javaapi.request.builder;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serdes;
import ru.tinkoff.gatling.kafka.request.builder.KafkaRequestBuilderBase;
import scala.reflect.ClassTag;

import static io.gatling.javaapi.core.internal.Expressions.*;

public class RROutTopicStep {

    private final String inputTopic;
    private final String outputTopic;

    public RROutTopicStep(String inputTopic, String outputTopic) {
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;
    }

    public <K, V> RequestReplyBuilder<?, ?> send(K key, V payload, Headers headers, Class<K> keyClass, Class<V> payloadClass) {
        return new RequestReplyBuilder<K, V>(KafkaRequestBuilderBase.apply(toStringExpression("myRequest")).requestReply()
                .requestTopic(toStringExpression(this.inputTopic))
                .replyTopic(toStringExpression(this.outputTopic))
                .send(
                        toStaticValueExpression(key),
                        toStaticValueExpression(payload),
                        toStaticValueExpression(headers),
                        Serdes.serdeFrom(keyClass),
                        ClassTag.apply(keyClass),
                        Serdes.serdeFrom(payloadClass),
                        ClassTag.apply(payloadClass)
                ));
    }
}
