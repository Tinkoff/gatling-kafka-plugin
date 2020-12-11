ThisBuild / organization := "ru.tinkoff"
ThisBuild / scalaVersion := "2.12.12"

ThisBuild / publishMavenStyle := true

ThisBuild / publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

ThisBuild / scmInfo := Some(
  ScmInfo(url("https://github.com/TinkoffCreditSystems/gatling-kafka-plugin"),
          "git@github.com:TinkoffCreditSystems/gatling-kafka-plugin.git"))

ThisBuild / developers := List(
  Developer(
    id = "jigarkhwar",
    name = "Ioann Akhaltsev",
    email = "i.akhaltsev@tinkoff.ru",
    url = url("https://github.com/jigarkhwar")
  )
)

ThisBuild / description := "Plugin to support kafka performance testing in Gatling(3.x.x)."
ThisBuild / licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / homepage := Some(url("https://github.com/TinkoffCreditSystems/gatling-kafka-plugin"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ =>
  false
}
