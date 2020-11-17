package ru.tinkoff.gatling.kafka.avroScheme

import java.io.File
import java.util

import com.github.imflog.schema.registry.RegistryClientWrapper
import com.github.imflog.schema.registry.download.{DownloadSubject, DownloadTaskAction}

object AvroSchemeDownloader {
  def download(url: String, topicName: String, version: Int, auth: String) = {
    val subject = new util.ArrayList[DownloadSubject]
    subject.add(new DownloadSubject(topicName, "", version))
    val dwnld = new DownloadTaskAction(RegistryClientWrapper.INSTANCE.client(url, auth), subject, new File("resources"))
    dwnld.run
  }
}
