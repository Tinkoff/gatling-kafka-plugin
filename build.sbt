import Dependencies._

lazy val root = (project in file("."))
  .enablePlugins(GitVersioning)
  .settings(
    name := "gatling-kafka-plugin",
    scalaVersion := "2.12.13",
    libraryDependencies ++= gatling,
    libraryDependencies ++= kafka,
    libraryDependencies ++= avro4s,
    libraryDependencies ++= gradleKafka,
    libraryDependencies ++= gradleAvro,
    libraryDependencies ++= gradle_logging,
    libraryDependencies ++= gradle_base,
    libraryDependencies ++= gradle_files_gradle_core,
    libraryDependencies ++= gradle_files_gradle_messaging,
    libraryDependencies ++= gradle_files_gradle_native,
    libraryDependencies ++= ant,
    resolvers ++= Seq(
      "Confluent" at "https://packages.confluent.io/maven/",
      "Gradle" at "https://plugins.gradle.org/m2/",
      "ivy" at "https://repo.lightbend.com/lightbend/ivy-releases/",
      "orgGradle" at "https://mvnrepository.com/artifact/org.gradle/",
      "files" at "https://repo.gradle.org/gradle/libs-releases-local/",
      "jcip" at "https://repository.mulesoft.org/nexus/content/repositories/public/",
      Resolver.sonatypeRepo("public")
    ),
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
