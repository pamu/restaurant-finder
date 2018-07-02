name := """restaurant-finder"""

version := "0.0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.jcenterRepo,
  "Artima Maven Repository" at "http://repo.artima.com/releases"
)

scalaVersion := "2.12.4"

scalacOptions ++= Seq("-Ypartial-unification")

libraryDependencies += ws
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.2.1"
libraryDependencies += "org.flywaydb" %% "flyway-play" % "4.0.0"
libraryDependencies += "org.typelevel" %% "cats-core" % "1.0.1"
libraryDependencies += "io.monix" %% "monix-execution" % "3.0.0-RC1"
libraryDependencies += "io.monix" %% "monix-eval" % "3.0.0-RC1"
libraryDependencies += "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1"
libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.3"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % "0.9.3")
libraryDependencies += "com.h2database" % "h2" % "1.4.196"

// test dependencies
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test"
