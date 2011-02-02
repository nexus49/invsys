import sbt._

class InvSysProject(info: ProjectInfo) extends DefaultWebProject(info)
{
  val snapshots = ScalaToolsSnapshots
  val lift = "net.liftweb" %% "lift-mapper" % "2.2-SNAPSHOT" % "compile"
  val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.25" % "test"
  // val h2 = "com.h2database" % "h2" % "1.2.121" % "runtime"
  // alternately use derby
  // val derby = "org.apache.derby" % "derby" % "10.2.2.0" % "runtime"
  val servlet = "javax.servlet" % "servlet-api" % "2.5" % "provided"
  val junit = "junit" % "junit" % "3.8.1" % "test"    
}