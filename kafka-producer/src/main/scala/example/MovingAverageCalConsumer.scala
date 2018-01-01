package example

import java.util
import java.util.Properties

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

object MovingAverageCalConsumer {
  def main(args: Array[String]): Unit = {
    if (args.length == 0) {
      println("MovingAverageCalConsumer {broker-list} {topic} {groupId} {timeout}")
      System.exit(1)
    }

    // Get arguments
    val zookeeperBrokerList: String = args(0)
    val topic: String = args(1)
    val groupId: String = args(2)
    val timeOut: Int = args(3).toInt
    val config = configure(zookeeperBrokerList, groupId)
    val consumer = new KafkaConsumer[String, String](config)

    consumer.subscribe(util.Collections.singletonList(topic))

    while (true) {
      var sum = 0
      val records = consumer.poll(50)
      records.asScala.flatMap(s => {
        try {
          Some(s.value.toInt)
        } catch {
          case e: NumberFormatException => None //do nothing
        }
      }).foreach(i => {
        println(i)
      })
    }

  }

  def configure(bootstrapServer: String, groupId: String): Properties = {
    val kafkaProps = new Properties()
    kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
    kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    kafkaProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
    kafkaProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
    kafkaProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")
    kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProps
  }
}
