# akka-word-counter

This is a project to demonstrate the use of Akka Actors.

## Manual Round Robin to count words

The package `wordcount.manual` contains an example of a program to count words on a list of strings.
Round Robin is handled manualy in class `Master`.

The sample contains a hierarchy with three actors:

- Supervisor
- Master
- Worker

`Supervisor` is at the top of the hierarchy, its responsability is receive the request and forward to other actor to count the words on the text.
'Master' is in the middle of the hierarchy, and its responsability is handle a set of actors to count the words in the text.
Finally, `Worker` actors are who really take care of the task.

`Master` actor send text to `Worker` actors in a Round Robin fashion.

This is just one possible solution of many.

## Round Robin using Akka implementation

Now the same task has been implement in package `wordcount.router` but as the name suggests we are using Akka Routers to simplify our job.
Basically the only change is in the `Master` actor. Instead of implement Round Robin ourselves we let Akka solve the task using Routers.
