package wordcount.router

import akka.actor.{Actor, ActorLogging}

object Worker {
  case class CountWords(text: String, task: Int)
}

class Worker extends Actor with ActorLogging{
  import Worker._

  val prefix = "[Worker]"
  val results: Map[Int, Int] = Map()

  override def receive: Receive = process(results)

  def process(r: Map[Int,Int]): Receive = {
    case CountWords(text, task) =>
      val words = text.split(" ").length
      log.info(s"$prefix task $task, found $words in text: $text")
      val newMapResults: Map[Int,Int] = r + (task -> words)
      context.become(process(newMapResults))

    case other =>
      log.info(s"$prefix Unknown message: ${other.toString}")
  }
}

