package kafkaex.producer

import java.util.{Date, Properties}

import kafkaex.{KafkaProxy, KafkaServerConfig, Producer, Role}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object KafkaExProducer {
  def apply(name: String)(implicit kafkaServer: KafkaServerConfig) = new KafkaExProducer(name)
}

class KafkaExProducer(override val name: String)(implicit val kafkaServer: KafkaServerConfig)
    extends KafkaProxy {

  private val props = new Properties()
  props.put("bootstrap.servers", kafkaServer)
  props.put("client.id", name)
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  override val role: Role = Producer()

  private val producer = new KafkaProducer[String, String](props)

  override def spawn(topic: String): Unit = {
    while (true) {
      val time = new Date().getTime.toString
      val data = new ProducerRecord[String, String](topic, time, time)
      Thread.sleep(1000)
      producer.send(data)
    }
  }
}
