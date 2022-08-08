resolvers ++= Seq(
  Resolver.bintrayIvyRepo("rallyhealth", "sbt-plugins"),
  // need for load sbt-schema-registry-plugin dependencies
  "Confluent" at "https://packages.confluent.io/maven/",
)

addSbtPlugin("io.gatling"     % "gatling-sbt"                % "4.2.3")
addSbtPlugin("com.github.sbt" % "sbt-ci-release"             % "1.5.10")
addSbtPlugin("org.scalameta"  % "sbt-scalafmt"               % "2.4.6")
addSbtPlugin("com.github.sbt" % "sbt-avro"                   % "3.4.2")
addSbtPlugin("ru.tinkoff"     % "sbt-schema-registry-plugin" % "0.2.0")

libraryDependencies += "org.apache.avro" % "avro-compiler" % "1.11.0"
