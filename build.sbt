name := "avro-producer-consumer-poc"

version := "0.1"

scalaVersion := "2.13.1"

resolvers += "confluent" at "http://packages.confluent.io/maven/"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka_2.11" % "0.10.0.0",
  "org.apache.avro" % "avro" % "1.9.1",
  "io.confluent" % "kafka-avro-serializer" % "5.3.0"
)
