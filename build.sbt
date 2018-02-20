organization in ThisBuild := "me.slotbook"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test

lazy val `slotbook` = (project in file("."))
  .aggregate(`company-api`, `company-impl`)
  //.enablePlugins(JavaAppPackaging)

lazy val `company-api` = (project in file("company-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `company-impl` = (project in file("company-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`company-api`)

lagomServiceLocatorPort in ThisBuild := 10000
lagomServiceGatewayPort in ThisBuild := 11000
