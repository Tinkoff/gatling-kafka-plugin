import sbt._

object Dependencies {
  private object Versions {
    // TODO: 7.3.1-ce (kafka 3.3) throw exception on test startup https://issues.apache.org/jira/browse/KAFKA-14270, will be fixed in kafka 3.4
    val kafka   = "7.3.0-ce"
    val gatling = "3.9.2"
    val avro4s  = "4.1.0"
    val avro    = "1.11.1"
  }

  lazy val gatling: Seq[ModuleID] = Seq(
    "io.gatling" % "gatling-core"      % Versions.gatling % "provided",
    "io.gatling" % "gatling-core-java" % Versions.gatling % "provided",
  )

  lazy val gatlingTest: Seq[ModuleID] = Seq(
    "io.gatling.highcharts" % "gatling-charts-highcharts" % Versions.gatling % "it,test",
    "io.gatling"            % "gatling-test-framework"    % Versions.gatling % "it,test",
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
    ("io.confluent" % "kafka-streams-avro-serde" % "7.3.0").exclude("org.apache.kafka", "kafka-streams-scala")
  lazy val avroSerializers: ModuleID = "io.confluent" % "kafka-avro-serializer" % "7.3.0"

}
