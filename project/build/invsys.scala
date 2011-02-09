import sbt._
import de.element34.sbteclipsify._

class InvSysProject(info: ProjectInfo) extends DefaultWebProject(info) with Eclipsify 
{
  val scalaVersion = "2.8.1"
  val liftVersion = "2.2"
  val scalateVersion = "1.1"
  val liftMongoDBVersion = "2.2"

  override def unmanagedClasspath =
    super.unmanagedClasspath +++ buildCompilerJar

  override def filterScalaJars = false

  override def libraryDependencies = Set(
	"org.scala-lang" % "scala-compiler" % scalaVersion % "compile->default",
  
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-mongodb" % liftMongoDBVersion,
    
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default",
    
    "junit" % "junit" % "4.5" % "test->default",
    "org.scalatest" % "scalatest" % "1.2",
    
    "ch.qos.logback" % "logback-classic" % "0.9.26",
    
    "net.liftweb" % ("lift-scalate_" + scalaVersion) % liftVersion % "compile->default"

) ++ super.libraryDependencies
}