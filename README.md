# gatling-kafka-plugin [![Maven Central](https://img.shields.io/maven-central/v/ru.tinkoff/gatling-kafka-plugin_2.12.svg?color=success)](https://search.maven.org/search?q=ru.tinkoff.gatling-kafka)

# Introduction

Plugin to support Kafka in Gatling

# Usage

### Getting Started

gatling-kafka-plugin is currently available for Scala 2.12

You may include plugin as dependency in project with your tests. Write 

```scala
libraryDependencies += "ru.tinkoff" %% "gatling-kafka-plugin" % <version> % Test
```


## Example Scenarios
Examples [here](https://github.com/TinkoffCreditSystems/gatling-kafka-plugin/tree/master/src/test/scala/ru/tinkoff/gatling/kafka/examples)

## Download Avro schema
To download avro-schema and create java classes you should add dependencies in `build.sbt`
 
```
     lazy val gradle_logging: Seq[ModuleID] = Seq(
       "org.gradle" % "gradle-logging"
     ).map(_ % "4.3" % "runtime")
   
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
   
     lazy val net_jcip: Seq[ModuleID] = Seq(
       "net.jcip" % "annotations"
     ).map(_ % "1.0")
```

To run you should create scala object in root project directory and type `sbt run`.

### Example download avro-schema
Example [here](https://github.com/TinkoffCreditSystems/gatling-kafka-plugin/tree/master/src/test/scala/ru/tinkoff/gatling/kafka/examples)
