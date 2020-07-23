package wordcount.router

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

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
      val workers = createWorkers(numWorkers)
      val router = Router(RoundRobinRoutingLogic(), workers)
      context.become(ready(router, 0))
      sender() ! Supervisor.WorkersReady

    case other =>
      log.info(s"$prefix Unknown message: ${other.toString}")
  }

  def ready(router: Router, currentTask: Int): Receive = {
    case ProcessLine(oneLine) =>
      log.info(s"$prefix currentTask=$currentTask, line to process has arrived: $oneLine")
      router.route(Worker.CountWords(oneLine, currentTask), sender())
      val nextTask = currentTask+1
      context.become(ready(router, nextTask))

    case Terminated(ref) =>
      log.info(s"$prefix actor has been terminated $ref")
      val newWorker = createWorkerActor
      context.watch(newWorker)
      router.addRoutee(newWorker)
      log.info(s"$prefix new actor has been created $newWorker")

    case other =>
      log.info(s"$prefix Unknown message: ${other.toString}")
  }

  def createWorkerActor = context.actorOf(Props[Worker])

  def createWorkers(n: Int) = {
    for (_<-1 to n) yield {
      val worker = createWorkerActor
      context.watch(worker)
      ActorRefRoutee(worker)
    }
  }

}

