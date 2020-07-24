package wordcount.manual

import akka.actor.{ActorSystem, Props}
import wordcount.data.WordCountData._

object TestWorkCounter extends App
{
  val system = ActorSystem("WordCountManualRouting")
  val wordCounter = system.actorOf(Props[Supervisor], "supervisor")

  wordCounter ! Supervisor.Start(5, listOfSentences)

  // Let's make a pause so all worker finishes his work
  Thread.sleep(1000)
  wordCounter ! Supervisor.DisplayResults
}
