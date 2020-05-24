ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"


val akkaVersion = "2.6.5"

val libDependencies = Seq(
   "com.typesafe.akka" %% "akka-persistence-typed" % akkaVersion,
    "com.typesafe.akka" %% "akka-persistence-query" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.github.dnvriend" %% "akka-persistence-jdbc" % "3.5.2",
    "mysql" % "mysql-connector-java" % "8.0.19",
    "com.typesafe.akka" %% "akka-serialization-jackson" % "2.6.4",
     "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
       "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "2.0.0",

    "org.scalatest" %% "scalatest" % "3.1.0" % Test,
)








