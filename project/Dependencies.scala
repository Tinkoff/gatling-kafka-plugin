import sbt._

object Dependencies {
  private object Versions {
    val kafka   = "7.2.3-ce"
    val gatling = "3.9.0"
    val avro4s  = "4.1.0"
    val avro    = "1.11.1"
  }

  lazy val gatling: Seq[ModuleID] = Seq(
    "io.gatling" % "gatling-core"           % Versions.gatling % "provided",
    "io.gatling" % "gatling-test-framework" % Versions.gatling % "provided",
  )

  lazy val kafka: Seq[ModuleID] = Seq(
    ("org.apache.kafka"  % "kafka-clients"       % Versions.kafka)
      .exclude("org.slf4j", "slf4j-api"),
    ("org.apache.kafka" %% "kafka-streams-scala" % Versions.kafka)
      .exclude("org.slf4j", "slf4j-api"),
  )

  lazy val avro4s: ModuleID = "com.sksamuel.avro4s" %% "avro4s-core" % Versions.avro4s % "provided"

  lazy val avroCompiler: ModuleID = "org.apache.avro" % "avro-compiler" % Versions.avro
  lazy val avroCore: ModuleID     = "org.apache.avro" % "avro"          % Versions.avro
  lazy val avroSerdes: ModuleID   =
    ("io.confluent" % "kafka-streams-avro-serde" % "7.2.2").exclude("org.apache.kafka", "kafka-streams-scala")

}
