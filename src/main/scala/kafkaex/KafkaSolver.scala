package kafkaex

import com.typesafe.scalalogging.Logger
import kafkaex.consumer.KafkaExConsumer
import kafkaex.producer.KafkaExProducer

case class KafkaServerConfig(server: String) {
  override def toString: String = server
}

object KafkaSolver {

  val logger = Logger(this.getClass)

  def spawn(topic: String)(implicit proxy: KafkaProxy) = {
    logger.info(proxy.info)
    proxy.spawn(topic)
  }

  def main(args: Array[String]): Unit = {

    if (args.length != 3) {
      logger.error("Not enough argument for specifying role, kafka server and topic.")
      System.exit(1)
    }

    implicit val kafkaServer = KafkaServerConfig(args(1))

    args(0) match {
      case "Producer" =>
        implicit val producer = KafkaExProducer("sample-kafka-producer")
        spawn(args(1))
      case "Consumer" =>
        implicit val consumer = KafkaExConsumer("sample-kafka-consumer", "sample-group")
        spawn(args(1))
    }

  }
}
