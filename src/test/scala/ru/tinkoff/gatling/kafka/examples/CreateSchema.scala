package ru.tinkoff.gatling.kafka.examples

import ru.tinkoff.gatling.kafka.avroScheme.{AvroSchemeClassesCreator, AvroSchemeDownloader}

object CreateSchema extends App {

  AvroSchemeDownloader.download("http://vm-kafka-schema-registry:8081", "testTopic", 1, "str")

  AvroSchemeClassesCreator.create("resources/testTopic.avsc", "src/test/scala")

}
