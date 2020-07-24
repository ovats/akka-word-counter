package wordcount.manual

import akka.actor.{Actor, ActorLogging}

object Worker {
  case class CountWords(text: String, task: Int)
  case object DisplayResultsWorker
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

    case DisplayResultsWorker =>
      log.info(s"$prefix DisplayResultsWorker $self")
      r.keys.foreach(key => log.info(s"task $key = ${r(key)} words worker $self"))

    case other =>
      log.info(s"$prefix Unknown message: ${other.toString}")
  }
}
