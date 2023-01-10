resolvers ++= Seq(
  Resolver.bintrayIvyRepo("rallyhealth", "sbt-plugins"),
  // need for load sbt-schema-registry-plugin dependencies
  "Confluent" at "https://packages.confluent.io/maven/",
)

addSbtPlugin("io.gatling"     % "gatling-sbt"                % "4.2.6")
addSbtPlugin("com.github.sbt" % "sbt-ci-release"             % "1.5.11")
addSbtPlugin("org.scalameta"  % "sbt-scalafmt"               % "2.5.0")
addSbtPlugin("com.github.sbt" % "sbt-avro"                   % "3.4.2")
addSbtPlugin("ru.tinkoff"     % "sbt-schema-registry-plugin" % "0.2.1")

libraryDependencies += "org.apache.avro" % "avro-compiler" % "1.11.1"
