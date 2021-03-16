package ru.tinkoff.gatling.kafka.avroScheme

import com.github.imflog.schema.registry.RegistryClientWrapper
import com.github.imflog.schema.registry.download.{DownloadSubject, DownloadTaskAction}

import java.io.File
import java.util

object AvroSchemeDownloader {
  def download(url: String, topicName: String, version: Int, auth: String): Int = {
    val subject = new util.ArrayList[DownloadSubject]
    subject.add(new DownloadSubject(topicName, "", version))
    val dwnld   = new DownloadTaskAction(RegistryClientWrapper.INSTANCE.client(url, auth), subject, new File("resources"))
    dwnld.run
  }
}
