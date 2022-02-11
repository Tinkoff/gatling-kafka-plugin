package ru.tinkoff.gatling.kafka.examples

import ru.tinkoff.gatling.kafka.avroScheme.AvroSchemeDownloader

/*
 Alternative way to download schemas from schema-registry without sbt task.
 Run this object to load schemas before testing
 */
object CreateSchema extends App {
  val downloader = AvroSchemeDownloader("http://test-schema-registry")

  downloader.schemaToFile("hello-world", 1)
}
