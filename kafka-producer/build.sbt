import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "exercise.java",
      scalaVersion := "2.12.3",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "kafka-producer",
    libraryDependencies += scalaTest % Test
  )

// https://mvnrepository.com/artifact/org.apache.kafka/kafka
libraryDependencies += "org.apache.kafka" %% "kafka" % "0.10.2.1"
libraryDependencies += "org.apache.kafka" % "kafka-streams" % "1.0.0"
