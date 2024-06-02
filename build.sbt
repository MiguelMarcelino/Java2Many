ThisBuild / scalaVersion := "3.3.0"

name := "Java2Many"
organization := "ch.epfl.scala"
version := "0.1"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "2.4.0",
  "org.eclipse.jdt" % "org.eclipse.jdt.core" % "3.37.0"
)
