import sbt._

object Dependencies {

  lazy val gatling: Seq[ModuleID] = Seq(
    "io.gatling" % "gatling-core" % "3.4.2" % "provided"
  )

  lazy val kafka: Seq[ModuleID] = Seq(
    ("org.apache.kafka" % "kafka-clients" % "2.2.2")
      .exclude("org.slf4j", "slf4j-api"))

  lazy val avro4s: Seq[ModuleID] = Seq(("com.sksamuel.avro4s" %% "avro4s-core" % "1.9.0" % "provided").exclude("org.slf4j", "slf4j-api"))

  lazy val gradleKafka: Seq[ModuleID] = Seq((
    "com.github.imflog.kafka-schema-registry-gradle-plugin" % "com.github.imflog.kafka-schema-registry-gradle-plugin.gradle.plugin" % "0.9.0").exclude("org.slf4j", "slf4j-api"))
//  ).map(_ % "1.1.0")

  lazy val gradleAvro: Seq[ModuleID] = Seq((
    "com.commercehub.gradle.plugin" % "gradle-avro-plugin" % "0.20.0").exclude("org.slf4j", "slf4j-api"))

  lazy val avroPlugin: Seq[ModuleID] = Seq((
    "org.apache.avro" % "avro-maven-plugin" % "1.09.0").exclude("org.slf4j", "slf4j-api"))

  lazy val gradle_logging: Seq[ModuleID] = Seq((
    "org.gradle" % "gradle-logging"  % "4.3" % "runtime").exclude("org.slf4j", "slf4j-api"))

  lazy val gradle_base: Seq[ModuleID] = Seq(
    "org.gradle" % "gradle-base-services"
  ).map(_ % "4.3-rc-4" % "runtime")

  lazy val gradle_files: Seq[ModuleID] = Seq(
    "org.gradle" % "gradle-core",
    "org.gradle" % "gradle-messaging",
    "org.gradle" % "gradle-native"
  ).map(_ % "6.1.1" % "runtime")

  lazy val ant: Seq[ModuleID] = Seq(
    "org.apache.ant" % "ant"
  ).map(_ % "1.8.2")

//  lazy val net_jcip: Seq[ModuleID] = Seq(
//    "net.jcip" % "annotations"
//  ).map(_ % "1.0")

}
