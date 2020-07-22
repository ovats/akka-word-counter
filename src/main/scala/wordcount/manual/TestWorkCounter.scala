package wordcount.manual

import akka.actor.{ActorSystem, Props}

object TestWorkCounter extends App
{
  val system = ActorSystem("WordCountManualRouting")
  val wordCounter = system.actorOf(Props[Supervisor], "supervisor")

  val listOfSentences = List(
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    "Suspendisse congue felis interdum leo cursus, non auctor dolor dictum.",
    "Duis urna nisl, gravida sed condimentum ac, consequat in lorem.",
    "Aenean ultrices ante mi, nec suscipit mi sagittis vitae.",
    "Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.",
    "Nam et leo eu lorem aliquet iaculis at non tortor.",
    "Aliquam ornare et dui vitae tincidunt.",
    "Donec scelerisque pulvinar tristique.",
    "Ut convallis pharetra ipsum, nec finibus nibh vestibulum vitae.",
    "Proin semper auctor consequat."
  )

  wordCounter ! Supervisor.Start(10, listOfSentences)
}
