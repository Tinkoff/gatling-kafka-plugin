addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.5")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "1.0.0")

resolvers += Resolver.bintrayIvyRepo("rallyhealth", "sbt-plugins")
addSbtPlugin("com.typesafe.sbt"    % "sbt-git"            % "1.0.0")
addSbtPlugin("com.rallyhealth.sbt" % "sbt-git-versioning" % "1.4.0")
