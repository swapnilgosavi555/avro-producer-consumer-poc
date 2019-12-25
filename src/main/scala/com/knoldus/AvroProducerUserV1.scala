package com.knoldus

import java.util.Properties
import java.nio.file.Files
import java.nio.file.Paths
import org.apache.avro.Schema.Parser
import org.apache.avro.generic.GenericData
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object AvroProducerUserV1 extends App {

  val props = new Properties
  props.put("bootstrap.servers", "localhost:9092")
  props.put(
    "key.serializer",
    "org.apache.kafka.common.serialization.StringSerializer"
  )
  props.put(
    "value.serializer",
    "io.confluent.kafka.serializers.KafkaAvroSerializer"
  )
  props.put("schema.registry.url", "http://localhost:8081")
  val key = "dev1"
  val topic = "userInfo"
  val schemaPath: String = "src/main/scala/com/knoldus/userV1.avsc"
  val valueSchemaString: String = new String(
    Files.readAllBytes(Paths.get(schemaPath))
  )
  val schemaParser = new Parser
  val valueSchemaAvro = schemaParser.parse(valueSchemaString)
  val avroRecord = new GenericData.Record(valueSchemaAvro)

  val userV1 = UserV1("Mary", "Pune", 555, "Green")
  avroRecord.put("name", userV1.name)
  avroRecord.put("location", userV1.location)
  avroRecord.put("favoriteNumber", userV1.favoriteNumber)
  avroRecord.put("favoriteColor", userV1.favoriteColor)

  val producer = new KafkaProducer[String, GenericData.Record](props)
  val record =
    new ProducerRecord[String, GenericData.Record](topic, key, avroRecord)
  try {

    producer.send(record).get()

  } catch {
    case ex: Exception =>
      ex.printStackTrace(System.out)
  } finally {

    producer.close()
  }

}
