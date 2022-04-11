package ru.tinkoff.gatling.kafka.checks

import io.gatling.commons.validation.Validation
import io.gatling.core.check.{Check, CheckResult}
import io.gatling.core.check.Check.PreparedCache
import io.gatling.core.session.{Expression, Session}
import ru.tinkoff.gatling.kafka.KafkaCheck
import ru.tinkoff.gatling.kafka.request.KafkaProtocolMessage

case class KafkaMessageCheck(wrapped: KafkaCheck) extends KafkaCheck {
  override def check(response: KafkaProtocolMessage, session: Session, preparedCache: PreparedCache): Validation[CheckResult] =
    wrapped.check(response, session, preparedCache)

  override def checkIf(condition: Expression[Boolean]): Check[KafkaProtocolMessage] = copy(wrapped.checkIf(condition))

  override def checkIf(condition: (KafkaProtocolMessage, Session) => Validation[Boolean]): Check[KafkaProtocolMessage] =
    copy(wrapped.checkIf(condition))
}
