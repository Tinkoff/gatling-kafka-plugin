package ru.tinkoff.gatling.kafka.actions

import io.gatling.commons.stats.OK
import io.gatling.commons.util.DefaultClock
import io.gatling.commons.validation.Validation
import io.gatling.core.CoreComponents
import io.gatling.core.action.{Action, ChainableAction}
import io.gatling.core.session._
import io.gatling.core.stats.StatsEngine
import io.gatling.core.util.NameGen
import org.apache.kafka.clients.consumer._
import ru.tinkoff.gatling.kafka.consumer.builder.ConsumerAttributes
import ru.tinkoff.gatling.kafka.protocol.consumer.KafkaConsumerProtocol

import java.util
import scala.jdk.CollectionConverters.IterableHasAsScala

class KafkaConsumeAction[K, V](
    val consumer: KafkaConsumer[K, V],
    val attr: ConsumerAttributes[K, V],
    val coreComponents: CoreComponents,
    val kafkaProtocol: KafkaConsumerProtocol,
    val throttled: Boolean,
    val next: Action
) extends ChainableAction with NameGen {

  override val name: String    = genName("kafkaRequest")
  val statsEngine: StatsEngine = coreComponents.statsEngine
  val clock                    = new DefaultClock

  override def execute(session: Session): Unit = recover(session) {

    attr.name(session).flatMap { name =>
      val outcome =
        poll(consumer, attr, throttled, session)

      outcome
        .onFailure(errorMessage => statsEngine.reportUnbuildableRequest(session.scenario, session.groups, name, errorMessage))

      outcome

    }

  }

  private def poll(
      consumer: Consumer[K, V],
      kafkaAttributes: ConsumerAttributes[K, V],
      throttled: Boolean,
      session: Session
  ): Validation[Unit] = {

    kafkaAttributes name session map { name =>
      consumer.subscribe(util.Arrays.asList(kafkaProtocol.topic))

      while (true) {

        val requestStartDate = clock.nowMillis

        val record = consumer.poll(kafkaAttributes.timeout).asScala

        for (data <- record.iterator)
          println(data.value())

        val requestEndDate = clock.nowMillis

        statsEngine.logResponse(
          session.scenario,
          session.groups,
          name,
          startTimestamp = requestStartDate,
          endTimestamp = requestEndDate,
          OK,
          None,
          None
        )

        coreComponents.throttler match {
          case Some(th) if throttled => th.throttle(session.scenario, () => next ! session)
          case _                     => next ! session
        }
      }

    }

  }

}
