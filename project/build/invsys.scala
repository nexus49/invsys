import sbt._
import de.element34.sbteclipsify._

class InvSysProject(info: ProjectInfo) extends DefaultWebProject(info) with Eclipsify {
  val liftVersion = "2.2"
  val scalateVersion = "1.3.2"
  // uncomment the following if you want to use the snapshot repo
  // val scalatoolsSnapshot = ScalaToolsSnapshots

  // If you're using JRebel for Lift development, uncomment
  // this line
  // override def scanDirectories = Nil

  override def unmanagedClasspath =
    super.unmanagedClasspath +++ buildCompilerJar

  override def filterScalaJars = false
  
  override def libraryDependencies = Set(
    "net.liftweb" % "lift-webkit_2.8.1" % liftVersion % "compile->default",
    "net.liftweb" % "lift-scalate_2.8.1" % liftVersion % "compile->default",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default",
    "junit" % "junit" % "4.5" % "test->default",
    "ch.qos.logback" % "logback-classic" % "0.9.26",
    "org.scala-lang" % "scala-compiler" % "2.8.1" % "compile->default",
    "org.scala-tools.testing" %% "specs" % "1.6.6" % "test->default",
    "org.fusesource.scalate" % "scalate-wikitext" % scalateVersion % "compile->default",
    "org.fusesource.scalate" % "scalate-page" % scalateVersion % "compile->default",
    "org.fusesource.scalamd" % "scalamd" % "1.4" % "compile->default"    
  ) ++ super.libraryDependencies
}