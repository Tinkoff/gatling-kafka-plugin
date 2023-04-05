# Gatling Kafka Plugin

![Build](https://github.com/TinkoffCreditSystems/gatling-kafka-plugin/workflows/Build/badge.svg) [![Maven Central](https://img.shields.io/maven-central/v/ru.tinkoff/gatling-kafka-plugin_2.13.svg?color=success)](https://search.maven.org/search?q=ru.tinkoff.gatling-kafka)  [![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)
[![codecov.io](https://codecov.io/github/Tinkoff/gatling-kafka-plugin/coverage.svg?branch=master)](https://codecov.io/github/Tinkoff/gatling-kafka-plugin?branch=master)

# Introduction

Plugin to support Kafka in Gatling (3.9.x)

# Usage

### Getting Started

Plugin is currently available for Scala 2.13, Java 17, Kotlin.

You may include plugin as dependency in project with your tests. Write

### Scala

```scala
libraryDependencies += "ru.tinkoff" %% "gatling-kafka-plugin" % <version> % Test
```

### Java

Write this to your dependencies block in build.gradle:

```java
gatling "ru.tinkoff:gatling-kafka-plugin_2.13:<version>"
```

### Kotlin

Write this to your dependencies block in build.gradle:

```kotlin
gatling("ru.tinkoff:gatling-kafka-plugin_2.13:<version>")
```

## Example Scenarios

### Scala

Examples [here](src/test/scala/ru/tinkoff/gatling/kafka/examples)

### Java

Examples [here](src/test/java/ru/tinkoff/gatling/kafka/javaapi/examples)

### Kotlin

Examples [here](src/test/kotlin/ru/tinkoff/gatling/kafka/javaapi/examples)

## Download and create Avro schema

Avro schema is downloaded using the plugin [sbt-schema-registry-plugin](https://github.com/Tinkoff/sbt-schema-registry-plugin)
and for that you need to configure schemas and url in `build.sbt` and run the command:

```bash 
sbt schemaRegistryDownload
```

To create java classes you should add use capabilities, that provide plugin [sbt-avro](https://github.com/sbt/sbt-avro).
This plugin is included in project and will do all needed for creating java classes in compile stage.
To run you should create scala object in root project directory and type `sbt run`.

### Example download avro-schema

Example [here](https://github.com/TinkoffCreditSystems/gatling-kafka-plugin/tree/master/src/test/scala/ru/tinkoff/gatling/kafka/examples)

## Avro support in Request-Reply

### Scala

To use avro messages as payload in key or value, you must:

- define implicit for schema registry url:

```scala
implicit val schemaRegUrl: String = "http://localhost:9094"
```

- or define serde for your class:

```scala
val ser =
  new KafkaAvroSerializer(
    new CachedSchemaRegistryClient("schRegUrl".split(',').toList.asJava, 16),
  )

val de =
  new KafkaAvroDeserializer(
    new CachedSchemaRegistryClient("schRegUrl".split(',').toList.asJava, 16),
  )

implicit val serdeClass: Serde[AvroClass] = new Serde[AvroClass] {
  override def serializer(): Serializer[AvroClass] = ser.asInstanceOf[Serializer[AvroClass]]
  override def deserializer(): Deserializer[AvroClass] = de.asInstanceOf[Deserializer[AvroClass]]
}
```

### Java

To use avro messages as payload in key or value, you must define serde for your class:

```java
public static Serializer<AvroClass> ser = (Serializer) new KafkaAvroSerializer(new CachedSchemaRegistryClient(Arrays.asList("schRegUrl".split(",")), 16));
public static Deserializer<AvroClass> de = (Deserializer) new KafkaAvroDeserializer(new CachedSchemaRegistryClient(Arrays.asList("schRegUrl".split(",")), 16));
```

### Kotlin

To use avro messages as payload in key or value, you must define serde for your class:

```kotlin
    val ser = KafkaAvroSerializer(CachedSchemaRegistryClient("schRegUrl".split(','), 16),) as Serializer<AvroClass>
val de = KafkaAvroDeserializer(CachedSchemaRegistryClient("schRegUrl".split(','), 16),) as Deserializer<AvroClass>
```

### Example usage Avro in Request-Reply

Example [scala](src/test/scala/ru/tinkoff/gatling/kafka/examples/AvroClassWithRequestReplySimulation.scala)

Example [java](src/test/java/ru/tinkoff/gatling/kafka/examples/AvroClassWithRequestReplySimulation.java)

Example [kotlin](src/test/kotlin/ru/tinkoff/gatling/kafka/examples/AvroClassWithRequestReplySimulation.kt)
