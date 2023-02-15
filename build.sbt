import Dependencies._
//import ru.tinkoff.load.avro.RegistrySubject

val scalaV      = "2.13.10"
val avroSchemas = Seq() // for example Seq(RegistrySubject("test-hello-schema", 1))

coverageEnabled := true

lazy val root = (project in file("."))
  .enablePlugins(GitVersioning, GatlingPlugin)
  .settings(
    name         := "gatling-kafka-plugin",
    scalaVersion := scalaV,
    libraryDependencies ++= gatling,
    libraryDependencies ++= gatlingTest,
    libraryDependencies ++= kafka,
    libraryDependencies ++= Seq(avro4s, avroCore, avroSerdes, avroSerializers),
    schemaRegistrySubjects ++= avroSchemas,
//    schemaRegistryUrl := "http://test-schema-registry:8081",
    resolvers ++= Seq(
      "Confluent" at "https://packages.confluent.io/maven/",
    ),
    resolvers ++= Resolver.sonatypeOssRepos("public"),
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",            // Option and arguments on same line
      "-Xfatal-warnings", // New lines for each options
      "-deprecation",
      "-feature",
      "-unchecked",
      "-language:implicitConversions",
      "-language:higherKinds",
      "-language:existentials",
      "-language:postfixOps",
    ),
  )
