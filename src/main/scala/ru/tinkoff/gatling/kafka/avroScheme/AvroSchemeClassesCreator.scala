package ru.tinkoff.gatling.kafka.avroScheme

import java.io.File

import org.apache.avro.compiler.specific.SchemaTask

object AvroSchemeClassesCreator {
  def create(targetSchema: String, outputDir: String): Unit = {
    val schema = new SchemaTask()
    schema.setFile(new File(targetSchema))
    schema.setDestdir(new File(outputDir))
    schema.execute()
  }
}
