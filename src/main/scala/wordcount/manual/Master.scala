package wordcount.manual

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object Master {
  case class InitializeWorkers(numWorkers: Int)
  case class ProcessLine(line: String)
}

class Master extends Actor with ActorLogging {
  import Master._

  val prefix = "[Master]"

  override def receive: Receive = {
    case InitializeWorkers(numWorkers) =>
      log.info(s"$prefix Initializing $numWorkers workers ...")
      val workers = for(i<-1 to numWorkers) yield createWorkerActor(i)
      context.become(ready(workers, 0, 0))
      sender() ! Supervisor.WorkersReady

    case other =>
      log.info(s"$prefix Unknown message: ${other.toString}")
  }

  def ready(workers: Seq[ActorRef], currentWorker: Int, currentTask: Int): Receive = {
    case ProcessLine(oneLine) =>
      log.info(s"$prefix currentWorker=$currentWorker currentTask=$currentTask, line to process has arrived: $oneLine")
      workers(currentWorker) ! Worker.CountWords(oneLine, currentTask)
      val nextWorker = (currentWorker+1) % workers.length
      val nextTask = currentTask+1
      context.become(ready(workers, nextWorker, nextTask))

    case other =>
      log.info(s"$prefix Unknown message: ${other.toString}")
  }

  def createWorkerActor(n: Int) = {
    context.actorOf(Props[Worker], s"worker_$n")
  }

}
