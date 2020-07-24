package wordcount.manual

import akka.actor.{Actor, ActorLogging, Props}

object Supervisor {
  case class Start(numWorkers: Int, text: List[String])
  case object WorkersReady
  case object DisplayResults
}

class Supervisor extends Actor with ActorLogging {
  import Supervisor._

  val prefix = "[Supervisor]"
  val master = createMasterActor

  override def receive: Receive = {
    case Start(numWorkers, text) =>
      log.info(s"$prefix Initializing $numWorkers workers ...")
      master ! Master.InitializeWorkers(numWorkers)
      context.become(initialized(text))

    case other =>
      log.info(s"$prefix Unknown message: ${other.toString}")
  }

  def initialized(text:List[String]): Receive = {
    case WorkersReady =>
      log.info(s"$prefix starting to process the text")
      text.foreach(oneLine => master ! Master.ProcessLine(oneLine))

    case DisplayResults =>
      log.info(s"$prefix Supervisor/DisplayResults")
      master ! Master.DisplayResults

    case other =>
      log.info(s"$prefix Unknown message: ${other.toString}")
  }

  def createMasterActor = context.actorOf(Props[Master])
}
