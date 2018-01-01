package example

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.util.Random

object SimpleCounterOldProducer {
  def main(args: Array[String]): Unit = {
    if (args.length == 0) {
      println("SimpleCounterOldProducer {broker-list} {topic} {sync} {delay (ms)} {count}")
      System.exit(1);
    }

    // Get arguments
    val brokerList: String = args(0)
    val topic: String = args(1)
    val sync: String = args(2)
    val delay: Int = args(3).toInt
    val count: Int = args(4).toInt

    val config = configure(brokerList, sync)

    // Start producer
    val producer = new KafkaProducer[String, String](config)

    val startTime = System.currentTimeMillis
    println("Starting...")

    val t = System.currentTimeMillis()
    Range(0, count).foreach(nEvents => {
      val rdm = new Random().nextInt(count)
      val data = new ProducerRecord[String, String](topic, null, s"$rdm")
      producer.send(data)
      Thread.sleep(delay)
    })


    val endTime = System.currentTimeMillis
    println("...we are done. This took " + (endTime - startTime) + " ms.")
    producer.close()
  }

  def configure(brokerList: String, sync: String): Properties = {
    val kafkaProps = new Properties()
    kafkaProps.put("bootstrap.servers", brokerList)
    kafkaProps.put("client.id", "ScalaProducerExample")
    kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    kafkaProps.put("request.required.acks", "1") // 1 - just the leader got it; -1 means leader and all replicas got it
    kafkaProps.put("producer.type", sync)
    kafkaProps
  }
}
