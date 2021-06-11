package ru.tinkoff.gatling.kafka.request.builder

import com.sksamuel.avro4s.{FromRecord, RecordFormat, SchemaFor}
import io.gatling.core.session.Expression
import org.apache.kafka.common.header.Header

import java.util

case class Avro4sAttributes[K, V](
    requestName: Expression[String],
    key: Option[Expression[K]],
    payload: Expression[V],
    schema: SchemaFor[V],
    format: RecordFormat[V],
    fromRecord: FromRecord[V],
    headers: Option[Expression[util.List[Header]]]
)
