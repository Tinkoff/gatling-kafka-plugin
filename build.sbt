import Dependencies._

lazy val root = (project in file("."))
  .enablePlugins(GitVersioning)
  .settings(
    name := "gatling-kafka-plugin",
    libraryDependencies ++= gatling,
    libraryDependencies ++= kafka,
    libraryDependencies ++= avro4s,
    libraryDependencies ++= gradleKafka,
    libraryDependencies ++= gradleAvro,
    libraryDependencies ++= gradle_logging,
    libraryDependencies ++= gradle_base,
    libraryDependencies ++= gradle_files,
    libraryDependencies ++= ant,
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
    )
  )
