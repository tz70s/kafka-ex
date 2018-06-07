package kafkaex.consumer

import java.util.{Collections, Properties}

import kafkaex.{Consumer, KafkaProxy, KafkaServerConfig}
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object KafkaExConsumer {
  def apply(name: String, groupId: String)(implicit kafkaServer: KafkaServerConfig) =
    new KafkaExConsumer(name, groupId)
}

class KafkaExConsumer(override val name: String, val groupId: String)(
  implicit val kafkaServer: KafkaServerConfig
) extends KafkaProxy {

  import scala.concurrent.ExecutionContext.Implicits.global

  val consumerProps = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")
    props.put(
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringDeserializer"
    )
    props.put(
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringDeserializer"
    )
    props
  }

  override val role = Consumer()

  @volatile var consumer = new KafkaConsumer[String, String](consumerProps)

  val peek = Future {
    consumer.poll(1000)
  }

  def spawn(topic: String) = {

    consumer.subscribe(Collections.singletonList(topic))

    while (true) {
      val records = Await.result(peek, 10.seconds)
      records.forEach { c =>
        println(s"KEY: ${c.key}, VALUE: ${c.value}, PARTITION: ${c.partition}, OFFSET: ${c.value}")
      }
      consumer.commitSync()
      Thread.sleep(1000)
    }
  }
}
