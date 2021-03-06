import sbt.Keys._
import play.sbt.PlaySettings

lazy val root = (project in file("."))
  .enablePlugins(PlayService, PlayLayoutPlugin)
  .settings(
    name := "tasklist-api",
    scalaVersion := "2.13.1",
    libraryDependencies ++= Seq(
      guice,
      "org.joda" % "joda-convert" % "2.2.1",
      "net.logstash.logback" % "logstash-logback-encoder" % "6.2",
      "io.lemonlabs" %% "scala-uri" % "1.5.1",
    ),
    // https://mvnrepository.com/artifact/mysql/mysql-connector-java
    libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.20",
    // https://mvnrepository.com/artifact/com.typesafe.play/play-slick
    libraryDependencies += "com.typesafe.play" %% "play-slick" % "5.0.0",
    libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0" % Test,
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
    
  )
// Documentation for this project:
//    sbt "project docs" "~ paradox"
//    open docs/target/paradox/site/index.html
lazy val docs = (project in file("docs")).enablePlugins(ParadoxPlugin).
  settings(
    scalaVersion := "2.13.1",
    paradoxProperties += ("download_url" -> "https://example.lightbend.com/v1/download/play-samples-play-scala-rest-api-example")
  )
