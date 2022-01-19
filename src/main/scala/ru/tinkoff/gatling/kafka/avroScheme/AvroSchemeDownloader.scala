package ru.tinkoff.gatling.kafka.avroScheme

import com.github.imflog.schema.registry.RegistryClientWrapper
import com.github.imflog.schema.registry.tasks.download.{DownloadSubject, DownloadTaskAction}

import java.io.File
import java.util
import scala.jdk.CollectionConverters.MapHasAsJava

object AvroSchemeDownloader {
  def download(url: String, topicName: String, version: Int, auth: String): Int = {

    val subject: util.ArrayList[DownloadSubject] = new util.ArrayList[DownloadSubject]

    subject.add(new DownloadSubject(topicName, "", version, false , null))

    val dwnld = new DownloadTaskAction(
      RegistryClientWrapper.INSTANCE.client(url, auth, Map.empty[String, String].asJava),
      new File("resources"),
      subject,
      true
    )

    dwnld.run
  }
}
