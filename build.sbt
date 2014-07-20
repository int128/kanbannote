name := "sbt-appengine-blank"

version := "1.0"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "javax.servlet" % "servlet-api" % "2.5" % "provided",
  "net.databinder" %% "unfiltered-filter" % "0.8.0",
  "net.databinder" %% "unfiltered-filter-uploads" % "0.8.0"
)

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.2.9",
  "com.evernote" % "evernote-api" % "1.25.1"
)
