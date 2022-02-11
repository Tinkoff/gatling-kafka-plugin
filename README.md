# gatling-kafka-plugin 
![Build](https://github.com/TinkoffCreditSystems/gatling-kafka-plugin/workflows/Build/badge.svg) [![Maven Central](https://img.shields.io/maven-central/v/ru.tinkoff/gatling-kafka-plugin_2.13.svg?color=success)](https://search.maven.org/search?q=ru.tinkoff.gatling-kafka)
# Introduction
Plugin to support Kafka in Gatling
# Usage
### Getting Started
gatling-kafka-plugin is currently available for Scala 2.13

You may include plugin as dependency in project with your tests. Write 
```scala
libraryDependencies += "ru.tinkoff" %% "gatling-kafka-plugin" % <version> % Test
```
## Example Scenarios
Examples [here](https://github.com/TinkoffCreditSystems/gatling-kafka-plugin/tree/master/src/test/scala/ru/tinkoff/gatling/kafka/examples)
## Download and create Avro schema
Avro schemas is downloaded using the plugin [sbt-schema-registry-plugin](https://github.com/Tinkoff/sbt-schema-registry-plugin)
and for that you need to configure schemas and url in `build.sbt` and run the command:
```bash 
sbt schemaRegistryDownload
```
To create java classes you should add use capabilities, that provide plugin [sbt-avro](https://github.com/sbt/sbt-avro).
This plugin is included in project and will do all needed for creating java classes in compile stage.
To run you should create scala object in root project directory and type `sbt run`.
### Example download avro-schema
Example [here](https://github.com/TinkoffCreditSystems/gatling-kafka-plugin/tree/master/src/test/scala/ru/tinkoff/gatling/kafka/examples)
