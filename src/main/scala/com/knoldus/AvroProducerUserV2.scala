package com.knoldus

import java.nio.file.{Files, Paths}
import java.util.Properties

import org.apache.avro.Schema.Parser
import org.apache.avro.generic.GenericData
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object AvroProducerUserV2 extends App {
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
  val schemaPath: String = "src/main/scala/com/knoldus/userV2.avsc"
  val valueSchemaString: String = new String(
    Files.readAllBytes(Paths.get(schemaPath))
  )
  val schemaParser = new Parser
  val valueSchemaAvro = schemaParser.parse(valueSchemaString)
  val avroRecord = new GenericData.Record(valueSchemaAvro)

  val userV2 = UserV2("Mary", "Pune", 555, "Green", "59", "Pizza")
  avroRecord.put("name", userV2.name)
  avroRecord.put("location", userV2.location)
  avroRecord.put("favoriteNumber", userV2.favoriteNumber)
  avroRecord.put("favoriteColor", userV2.favoriteColor)
  avroRecord.put("friends", userV2.friends)
  avroRecord.put("favoriteDish", userV2.favoriteDish)

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
