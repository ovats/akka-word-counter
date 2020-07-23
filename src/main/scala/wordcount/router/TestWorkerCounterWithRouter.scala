package wordcount.router

import akka.actor.{ActorSystem, Props}
import wordcount.data.WordCountData._


object TestWorkerCounterWithRouter extends App {

  val system = ActorSystem("WordCountAkkaRouting")
  val wordCounter = system.actorOf(Props[Supervisor], "supervisor")

  wordCounter ! Supervisor.Start(3, listOfSentences)

}
