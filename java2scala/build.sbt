ThisBuild / scalaVersion := "3.3.0"

name := "Java2Scala"
organization := "ch.epfl.scala"
version := "0.1"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.1",
  "org.eclipse.jdt" % "org.eclipse.jdt.core" % "3.12.2",
  "io.github.portfoligno" % "javaparser-for-scala" % "1,6.2"
)
