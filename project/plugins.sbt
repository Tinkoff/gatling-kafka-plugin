resolvers ++= Seq(
  Resolver.bintrayIvyRepo("rallyhealth", "sbt-plugins"),
  // need for load sbt-schema-registry-plugin dependencies
  "Confluent" at "https://packages.confluent.io/maven/",
)

addSbtPlugin("com.github.sbt" % "sbt-ci-release"             % "1.5.12")
addSbtPlugin("io.gatling"     % "gatling-sbt"                % "4.3.2")
addSbtPlugin("org.scalameta"  % "sbt-scalafmt"               % "2.5.0")
addSbtPlugin("com.github.sbt" % "sbt-avro"                   % "3.4.2")
addSbtPlugin("ru.tinkoff"     % "sbt-schema-registry-plugin" % "0.2.1")
addSbtPlugin("org.scoverage"  % "sbt-scoverage"              % "2.0.8")

libraryDependencies += "org.apache.avro" % "avro-compiler" % "1.11.1"
