libraryDependencies += "org.mortbay.jetty" % "jetty" % "6.1.22" % "container"

libraryDependencies ++= Seq(
"com.google.appengine" % "appengine-api-1.0-sdk" % "1.9.6",
"com.google.appengine" % "appengine-api-labs" % "1.9.6"
)

appengineSettings

lazy val reloadDevServer = taskKey[Unit]("Reloads App Engine development server")

reloadDevServer := {
  (webappResources in Compile).value.foreach { webappResource =>
    val appengineWebXml = webappResource / "WEB-INF" / "appengine-web.xml"
    appengineWebXml.setLastModified(System.currentTimeMillis())
  }
}

packageWar <<= (packageWar in Compile) dependsOn reloadDevServer
