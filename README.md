# gatling-kafka-plugin 
![Build](https://github.com/TinkoffCreditSystems/gatling-kafka-plugin/workflows/Build/badge.svg) [![Maven Central](https://img.shields.io/maven-central/v/ru.tinkoff/gatling-kafka-plugin_2.12.svg?color=success)](https://search.maven.org/search?q=ru.tinkoff.gatling-kafka)
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
```scala
     libraryDependencies ++= Seq(
       "org.gradle" % "gradle-logging"
     ).map(_ % "4.3" % "runtime"),
    
     libraryDependencies ++= Seq(
       "org.gradle" % "gradle-base-services"
     ).map(_ % "4.3-rc-4" % "runtime"),
    
     libraryDependencies ++= Seq(
       "org.gradle" % "gradle-core",
       "org.gradle" % "gradle-messaging",
       "org.gradle" % "gradle-native"
     ).map(_ % "6.1.1" % "runtime"),
    
     libraryDependencies ++= Seq(
       "org.apache.ant" % "ant"
     ).map(_ % "1.8.2"),
    
     libraryDependencies ++= Seq(
       "net.jcip" % "annotations"
     ).map(_ % "1.0"),
    
     libraryDependencies ++= Seq(
       "org.apache.avro" % "avro-maven-plugin"
     ).map(_ % "1.10.0"),
    
     resolvers ++= Seq(
       "Confluent" at "https://packages.confluent.io/maven/",
       "Gradle" at "https://plugins.gradle.org/m2/",
       "ivy" at "https://repo.lightbend.com/lightbend/ivy-releases/",
       "orgGradle" at "https://mvnrepository.com/artifact/org.gradle/",
       "files" at "https://repo.gradle.org/gradle/libs-releases-local/",
       "jcip" at "https://repository.mulesoft.org/nexus/content/repositories/public/",
       Resolver.sonatypeRepo("public")
     )
```
To run you should create scala object in root project directory and type `sbt run`.
### Example download avro-schema
Example [here](https://github.com/TinkoffCreditSystems/gatling-kafka-plugin/tree/master/src/test/scala/ru/tinkoff/gatling/kafka/examples)
