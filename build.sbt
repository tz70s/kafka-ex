name := "kafka-ex"

lazy val settings = Seq(
  version := "0.1",
  scalaVersion := "2.12.6"
)

lazy val deps = Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.apache.kafka" % "kafka-clients" % "1.1.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
)

lazy val root = (project in file("."))
  .settings(
    settings,
    libraryDependencies ++= deps
  )

