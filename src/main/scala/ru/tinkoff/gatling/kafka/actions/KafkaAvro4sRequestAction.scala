package ru.tinkoff.gatling.kafka.actions

import io.gatling.commons.stats.{KO, OK}
import io.gatling.commons.util.DefaultClock
import io.gatling.commons.validation.Validation
import io.gatling.core.CoreComponents
import io.gatling.core.action.{Action, ExitableAction}
import io.gatling.core.session.Session
import io.gatling.core.stats.StatsEngine
import io.gatling.core.util.NameGen
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}
import ru.tinkoff.gatling.kafka.protocol.KafkaProtocol
import ru.tinkoff.gatling.kafka.request.builder.Avro4sAttributes

class KafkaAvro4sRequestAction[K, V](val producer: KafkaProducer[K, GenericRecord],
                                     val attr: Avro4sAttributes[K, V],
                                     val coreComponents: CoreComponents,
                                     val kafkaProtocol: KafkaProtocol,
                                     val throttled: Boolean,
                                     val next: Action)
    extends ExitableAction with NameGen {

  val statsEngine: StatsEngine = coreComponents.statsEngine
  val clock                    = new DefaultClock
  override val name: String    = genName("kafkaAvroRequest")

  override def execute(session: Session): Unit = recover(session) {
    attr requestName session flatMap { requestName =>
      val outcome = sendRequest(requestName, producer, attr, throttled, session)

      outcome.onFailure(
        errorMessage => statsEngine.reportUnbuildableRequest(session, requestName, errorMessage)
      )

      outcome
    }
  }

  def sendRequest(requestName: String,
                  producer: KafkaProducer[K, GenericRecord],
                  attr: Avro4sAttributes[K, V],
                  throttled: Boolean,
                  session: Session): Validation[Unit] = {

    attr payload session map { payload =>
      val record = attr.key
        .map(k => new ProducerRecord(kafkaProtocol.topic, k(session).toOption.get, attr.format.to(payload)))
        .getOrElse(new ProducerRecord(kafkaProtocol.topic, attr.format.to(payload)))

      val requestStartDate = clock.nowMillis

      producer.send(
        record,
        (_: RecordMetadata, e: Exception) => {

          val requestEndDate = clock.nowMillis
          statsEngine.logResponse(
            session,
            requestName,
            requestStartDate,
            requestEndDate,
            if (e == null) OK else KO,
            None,
            if (e == null) None else Some(e.getMessage)
          )

          if (throttled) {
            coreComponents.throttler.throttle(session.scenario, () => next ! session)
          } else {
            next ! session
          }

        }
      )

    }

  }
}
