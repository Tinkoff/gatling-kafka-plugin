# gatling-kafka

# Introduction

Plugin for support Kafka in Gatling

# Usage

## Build 
For local usage clone project from github repository and run in project folder `sbt publishLocal`. 

After that you may include plugin as dependency in project with your tests. Write 
`libraryDependencies += "ru.tinkoff" %% "gatling-kafka-plugin" % <version> % Test` in build.sbt

## Example Scenarios
Examples in \src\test\scala\ru\tinkoff\gatling\kafka\examples\
