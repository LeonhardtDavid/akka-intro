name := "akka-intro"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.10"

libraryDependencies ++= {
  val akkaVersion = "2.5.25"

  Seq(
    "ch.qos.logback"    %  "logback-classic" % "1.2.3",
    "com.typesafe.akka" %% "akka-slf4j"      % akkaVersion,
    "com.typesafe.akka" %% "akka-actor"      % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit"    % akkaVersion  % Test,
    "org.scalatest"     %% "scalatest"       % "3.0.8"      % Test
  )
}
