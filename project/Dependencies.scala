import sbt._

object Dependencies {
  private object Versions {
    val gatling      = "3.7.4"
    val kafka        = "2.8.0"
    val avro4s       = "4.0.12"
    val avro         = "1.11.0"
    val schReqClient = "7.0.1"
  }

  lazy val gatling: Seq[ModuleID] = Seq(
    "io.gatling" % "gatling-core" % Versions.gatling % "provided",
  )

  lazy val kafka: Seq[ModuleID] = Seq(
    ("org.apache.kafka" % "kafka-clients" % Versions.kafka)
      .exclude("org.slf4j", "slf4j-api"),
  )

  lazy val avro4s: ModuleID = "com.sksamuel.avro4s" %% "avro4s-core" % Versions.avro4s % "provided"

  lazy val avroCompiler: ModuleID = "org.apache.avro" % "avro-compiler" % Versions.avro
  lazy val avroCore: ModuleID     = "org.apache.avro" % "avro"          % Versions.avro

  lazy val schemaRegistryClient: ModuleID = "io.confluent" % "kafka-schema-registry-client" % Versions.schReqClient

}
