organization in ThisBuild := "me.slotbook"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test

lazy val `slotbook-api` = (project in file("."))
  .aggregate(`slotbook-api-api`, `slotbook-api-impl`, `slotbook-api-stream-api`, `slotbook-api-stream-impl`)

lazy val `slotbook-api-api` = (project in file("slotbook-api-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `slotbook-api-impl` = (project in file("slotbook-api-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`slotbook-api-api`)

lazy val `slotbook-api-stream-api` = (project in file("slotbook-api-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `slotbook-api-stream-impl` = (project in file("slotbook-api-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`slotbook-api-stream-api`, `slotbook-api-api`)
