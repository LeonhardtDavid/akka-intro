name := "akka-intro"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.11"

libraryDependencies ++= {
  val akkaVersion = "2.6.6"

  Seq(
    "ch.qos.logback"    %  "logback-classic" % "1.2.3",
    "com.typesafe.akka" %% "akka-slf4j"      % akkaVersion,
    "com.typesafe.akka" %% "akka-actor"      % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit"    % akkaVersion  % Test,
    "org.scalatest"     %% "scalatest"       % "3.1.1"      % Test
  )
}
