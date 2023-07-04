ThisBuild / organization := "com.shotexa"
ThisBuild / scalaVersion := "3.3.0"

lazy val `tapir-prometheus-metrics-test` =
  project
    .in(file("."))
    .settings(name := "tapir-prometheus-metrics-test")
    .settings(dependencies)

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    "dev.zio" %% "zio" % "2.0.14",
    "dev.zio" %% "zio-metrics-connectors" % "2.0.6",
    "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % "1.6.0", // "1.2.3",
    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server-zio" % "1.6.0", // "1.2.3",
    "org.http4s" %% "http4s-blaze-server" % "0.23.12",
    "com.softwaremill.sttp.tapir" %% "tapir-prometheus-metrics" % "1.6.0" // this dep breaks the server if other tapir libs don't match its version (1.6.0)
  )
)
