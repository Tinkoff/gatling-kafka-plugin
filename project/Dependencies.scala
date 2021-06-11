import sbt._

object Dependencies {

  lazy val gatling: Seq[ModuleID] = Seq(
    "io.gatling" % "gatling-core" % "3.5.1" % "provided"
  )

  lazy val epoll: Seq[ModuleID] = Seq(
    "io.netty" % "netty-transport-native-epoll"  % "4.1.58.Final" classifier "linux-x86_64",
    "io.netty" % "netty-transport-native-kqueue" % "4.1.58.Final" classifier "osx-x86_64"
  )

  lazy val kafka: Seq[ModuleID] = Seq(
    ("org.apache.kafka" % "kafka-clients" % "2.7.0")
      .exclude("org.slf4j", "slf4j-api")
  )

  lazy val avro4s: Seq[ModuleID] = Seq(
    "com.sksamuel.avro4s" %% "avro4s-core"           % "4.0.4" % "provided",
    "io.confluent"         % "kafka-avro-serializer" % "5.3.0"
  )

  lazy val gradleKafka: Seq[ModuleID]    = Seq(
    "com.github.imflog" % "kafka-schema-registry-gradle-plugin"
  ).map(_ % "1.1.1")

  lazy val gradleAvro: Seq[ModuleID]     = Seq(
    "com.commercehub.gradle.plugin" % "gradle-avro-plugin"
  ).map(_ % "0.99.99")

  lazy val avroPlugin: Seq[ModuleID]     = Seq(
    "org.apache.avro" % "avro-maven-plugin"
  ).map(_ % "1.09.0")

  lazy val gradle_logging: Seq[ModuleID] = Seq(
    "org.gradle" % "gradle-logging"
  ).map(_ % "4.3" % "runtime")

  lazy val gradle_base: Seq[ModuleID]    = Seq(
    "org.gradle" % "gradle-base-services"
  ).map(_ % "4.3-rc-4" % "runtime")

  lazy val gradle_files: Seq[ModuleID]   = Seq(
    "org.gradle" % "gradle-core",
    "org.gradle" % "gradle-messaging",
    "org.gradle" % "gradle-native"
  ).map(_ % "6.1.1" % "runtime")

  lazy val ant: Seq[ModuleID]            = Seq(
    "org.apache.ant" % "ant"
  ).map(_ % "1.8.2")

  lazy val net_jcip: Seq[ModuleID]       = Seq(
    "net.jcip" % "annotations"
  ).map(_ % "1.0")

}
