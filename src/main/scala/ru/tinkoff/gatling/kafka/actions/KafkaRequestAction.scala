package ru.tinkoff.gatling.kafka.actions

import io.gatling.commons.stats.{KO, OK}
import io.gatling.commons.util.DefaultClock
import io.gatling.commons.validation.Validation
import io.gatling.core.CoreComponents
import io.gatling.core.action.{Action, ExitableAction}
import io.gatling.core.session._
import io.gatling.core.stats.StatsEngine
import io.gatling.core.util.NameGen
import org.apache.kafka.clients.producer._
import ru.tinkoff.gatling.kafka.protocol.KafkaProtocol
import ru.tinkoff.gatling.kafka.request.builder.KafkaAttributes

class KafkaRequestAction[K, V](val producer: KafkaProducer[K, V],
                               val attr: KafkaAttributes[K, V],
                               val coreComponents: CoreComponents,
                               val kafkaProtocol: KafkaProtocol,
                               val throttled: Boolean,
                               val next: Action)
    extends ExitableAction with NameGen {

  override val name: String    = genName("kafkaRequest")
  val statsEngine: StatsEngine = coreComponents.statsEngine
  val clock                    = new DefaultClock

  override def execute(session: Session): Unit = recover(session) {

    attr requestName session flatMap { requestName =>
      val outcome =
        sendRequest(requestName, producer, attr, throttled, session)

      outcome.onFailure(
        errorMessage => statsEngine.reportUnbuildableRequest(session, requestName, errorMessage)
      )

      outcome

    }

  }

  private def sendRequest(requestName: String,
                          producer: Producer[K, V],
                          kafkaAttributes: KafkaAttributes[K, V],
                          throttled: Boolean,
                          session: Session): Validation[Unit] = {

    kafkaAttributes payload session map { payload =>
      val record = kafkaAttributes.key match {
        case Some(k) =>
          new ProducerRecord[K, V](kafkaProtocol.topic, k(session).toOption.get, payload)
        case None =>
          new ProducerRecord[K, V](kafkaProtocol.topic, payload)
      }

      val requestStartDate = clock.nowMillis

      producer.send(
        record,
        (_: RecordMetadata, e: Exception) => {

          val requestEndDate = clock.nowMillis
          statsEngine.logResponse(
            session,
            requestName,
            startTimestamp = requestStartDate,
            endTimestamp = requestEndDate,
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
