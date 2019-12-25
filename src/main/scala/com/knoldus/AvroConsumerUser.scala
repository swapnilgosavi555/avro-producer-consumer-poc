package com.knoldus

import java.util
import java.util.Properties
import scala.jdk.CollectionConverters._
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.KafkaConsumer

object AvroConsumerUser extends App {

  val props = new Properties()
  props.put("group.id", "test")
  props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094")
  props.put(
    "key.deserializer",
    "org.apache.kafka.common.serialization.StringDeserializer"
  )
  props.put(
    "value.deserializer",
    "io.confluent.kafka.serializers.KafkaAvroDeserializer"
  )
  props.put("schema.registry.url", "http://localhost:8081")
  val consumer = new KafkaConsumer[String, GenericRecord](props)
  try {
    consumer.subscribe(util.Arrays.asList("userInfo"))
    while (true) {
      val records = consumer.poll(1000).asScala.iterator
      for (record <- records) {
        val person = record.value()
        println(person)
      }
    }
  } catch {
    case e: Exception => e.printStackTrace()
  } finally {
    consumer.close()
  }
}
