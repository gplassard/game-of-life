ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "game-of-life",
    idePackagePrefix := Some("fr.gplassard.gol")
  )

libraryDependencies += "org.scalafx" %% "scalafx" % "23.0.1-R34"
