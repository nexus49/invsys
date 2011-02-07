import sbt._
import de.element34.sbteclipsify._

class InvSysProject(info: ProjectInfo) extends DefaultWebProject(info) with Eclipsify {
  val liftVersion = "2.2"
  val scalaVersion = "2.8.1"

  // uncomment the following if you want to use the snapshot repo
  // val scalatoolsSnapshot = ScalaToolsSnapshots

  // If you're using JRebel for Lift development, uncomment
  // this line
  // override def scanDirectories = Nil

  override def unmanagedClasspath =
    super.unmanagedClasspath +++ buildCompilerJar

  override def filterScalaJars = false

  override def libraryDependencies = Set(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-record" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-squeryl-record" % liftVersion % "compile->default",
    "org.scala-lang" % "scala-compiler" % scalaVersion % "compile->default",
    "org.squeryl" % "squeryl_2.8.1" % "0.9.4-RC3"  % "compile->default",
    "cglib" % "cglib-nodep" % "2.2",
    "ch.qos.logback" % "logback-classic" % "0.9.26" % "compile->default",
    "junit" % "junit" % "4.5" % "test->default",
    "org.scalatest" % "scalatest" % "1.2",
    "org.scala-tools.testing" % "specs_2.8.0" % "1.6.5",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default",
    "com.h2database" % "h2" % "1.3.148"

	) ++ super.libraryDependencies
}