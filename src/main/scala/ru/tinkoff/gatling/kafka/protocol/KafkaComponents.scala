package ru.tinkoff.gatling.kafka.protocol

import io.gatling.core.CoreComponents
import io.gatling.core.protocol.ProtocolComponents
import io.gatling.core.session.Session

case class KafkaComponents(coreComponents: CoreComponents, kafkaProtocol: KafkaProtocol) extends ProtocolComponents {

  override def onStart: Session => Session = ProtocolComponents.NoopOnStart

  override def onExit: Session => Unit = ProtocolComponents.NoopOnExit
}
