import sbt._

object Dependencies {

  lazy val gatling: Seq[ModuleID] = Seq(
    "io.gatling" % "gatling-core"
  ).map(_ % "3.1.3" % "provided")

  lazy val kafka: Seq[ModuleID] = Seq(
    ("org.apache.kafka" % "kafka-clients" % "2.2.0")
      .exclude("org.slf4j", "slf4j-api"))

  lazy val avro4s: Seq[ModuleID] = Seq(
    "com.sksamuel.avro4s" %% "avro4s-core" % "1.9.0")

}
