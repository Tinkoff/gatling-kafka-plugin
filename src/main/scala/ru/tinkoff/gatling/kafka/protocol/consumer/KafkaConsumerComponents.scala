package ru.tinkoff.gatling.kafka.protocol.consumer

import io.gatling.core.CoreComponents
import io.gatling.core.protocol.ProtocolComponents
import io.gatling.core.session.Session

case class KafkaConsumerComponents(coreComponents: CoreComponents, kafkaProtocol: KafkaConsumerProtocol) extends ProtocolComponents {

  override def onStart: Session => Session = Session.Identity

  override def onExit: Session => Unit = ProtocolComponents.NoopOnExit
}
