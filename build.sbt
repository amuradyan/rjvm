ThisBuild / scalaVersion := "3.1.0"
ThisBuild / organization := "amuradyan"
ThisBuild / name := "Coding with Rock the JVM"

val rjvm = project
  .in(file("."))
  .settings(
    libraryDependencies += "org.tpolecat" %% "typename" % "1.0.0"
  )
