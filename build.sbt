name := "akka-word-counter"

version := "1.0.2"

scalaVersion := "2.12.7"

val akkaVersion = "2.5.13"

libraryDependencies ++=Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion
)