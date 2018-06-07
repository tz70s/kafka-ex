package kafkaex

import com.typesafe.scalalogging.Logger

sealed trait Role

case class Producer() extends Role {
  override def toString = "producer"
}

case class Consumer() extends Role {
  override def toString = "consumer"
}

/** Trait for defining kafka proxy for encapsulating which roles of consumer or producer. */
trait KafkaProxy {

  val name: String

  val role: Role

  protected val logger: Logger = Logger(this.getClass)

  def info = s"Spawning Kafka $role - $name"

  def spawn(topic: String)
}
