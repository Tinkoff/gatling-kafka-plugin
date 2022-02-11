package ru.tinkoff.gatling.kafka.avroScheme

import io.confluent.kafka.schemaregistry.client.rest.entities.Schema
import io.confluent.kafka.schemaregistry.client.{CachedSchemaRegistryClient, SchemaRegistryClient}

import java.nio.file.{Files, Path, Paths}
import scala.util.Try

class AvroSchemeDownloader(client: SchemaRegistryClient, schemaOutputDir: Path) {

  private def createOutputDirIfNeeded: Try[Unit] = Try {
    if (Files.notExists(schemaOutputDir)) {
      Files.createDirectory(schemaOutputDir)
    }
  }

  private def fileNameFromSchema(schema: Schema): Try[String] =
    scala.util.Success(s"${schema.getSubject}-${schema.getVersion}.${AvroSchemeDownloader.avroSchemeFileExtension}")

  private def writeSchemaToFile(schema: Schema, fileName: String) = Try {
    Files.write(schemaOutputDir.resolve(fileName), schema.getSchema.getBytes())
  }

  def schemaToFile(name: String, version: Int): Unit = {
    (for {
      schema   <- Try(client.getByVersion(name, version, false))
      _        <- createOutputDirIfNeeded
      fileName <- fileNameFromSchema(schema)
      path     <- writeSchemaToFile(schema, fileName)
    } yield path).fold(e => e.printStackTrace(), p => println(s"schema $name saved to $p"))
  }
}

object AvroSchemeDownloader {

  val avroSchemeFileExtension = "avsc"

  def apply(rootUrl: String): AvroSchemeDownloader =
    new AvroSchemeDownloader(new CachedSchemaRegistryClient(rootUrl, 200), Paths.get("./src/main/avro"))
}
