ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

val http4sVersion = "0.23.24"
val jwtHttp4sVersion = "1.2.0"
val jwtScalaVersion = "9.3.0"


lazy val root = (project in file("."))
  .settings(
    name := "HttpAuthentication-http4s"
  ).settings(
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      "dev.profunktor" %% "http4s-jwt-auth" % jwtHttp4sVersion,
      "com.github.jwt-scala" %% "jwt-core" % jwtScalaVersion,
      "com.github.jwt-scala" %% "jwt-circe" % jwtScalaVersion
    )
  )
