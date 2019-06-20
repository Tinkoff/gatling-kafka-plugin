import Dependencies._

lazy val root = (project in file("."))
  .settings(
    inThisBuild(
      List(
        organization := "ru.tinkoff",
        scalaVersion := "2.12.8",
        version := "0.1.0"
      )),
    name := "gatling-kafka-plugin",
    libraryDependencies ++= gatling,
    libraryDependencies ++= kafka,
    libraryDependencies ++= avro4s,
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8", // Option and arguments on same line
      "-Xfatal-warnings", // New lines for each options
      "-deprecation",
      "-feature",
      "-unchecked",
      "-language:implicitConversions",
      "-language:higherKinds",
      "-language:existentials",
      "-language:postfixOps"
    ),
    homepage := Some(url("https://github.com/TinkoffCreditSystems/gatling-kafka-plugin")),
    scmInfo := Some(ScmInfo(url("https://github.com/TinkoffCreditSystems/gatling-kafka-plugin"), "git@github.com:TinkoffCreditSystems/gatling-kafka-plugin.git")),
    developers := List(Developer("jigarkhwar", "Ioann Akhaltsev", "i.akhaltsev@tinkoff.ru", url("https://github.com/Jijka"))),
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
    publishMavenStyle := true,
    // Add sonatype repository settings
    publishTo := Some(
      if (isSnapshot.value)
        Opts.resolver.sonatypeSnapshots
      else
        Opts.resolver.sonatypeStaging
    )
  )
