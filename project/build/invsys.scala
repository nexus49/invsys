import sbt._
import de.element34.sbteclipsify._

class InvSysProject(info: ProjectInfo) extends DefaultWebProject(info) with Eclipsify 
{
  // nice version hell, here we go:
  // lift-scalate depends on scalate 1.1:
  //    https://github.com/lift/lift/blob/Lift-2.2-2.8-release/framework/lift-modules/lift-scalate/pom.xml
  // scalate 1.1 does not seem to work with scala 2.8.1
  //   java.lang.NoSuchMethodError: scala.collection.TraversableLike.withFilter(Lscala/Function1;)Lscala/collection/TraversableLike$WithFilter;
  //	at org.fusesource.scalate.util.ClassPathBuilder$.getClassPathFrom(ClassPathBuilder.scala:91) ~[scalate-core-1.1.jar:1.1]
  //	at org.fusesource.scalate.util.ClassPathBuilder.addPathFrom(ClassPathBuilder.scala:45) ~[scalate-core-1.1.jar:1.1]
  //	at org.fusesource.scalate.util.ClassPathBuilder.addPathFrom(ClassPathBuilder.scala:40) ~[scalate-core-1.1.jar:1.1]
  //	at net.liftweb.scalate.LiftTemplateEngine.buildClassPath(LiftTemplateEngine.scala:37) ~[lift-scalate_2.8.1-2.2.jar:2.2]
 
  // ==> using scala 2.8.0 for now
  // ==> still not working :(
	
  val scalaVersion = "2.8.0"
  val liftVersion = "2.2"
  val scalateVersion = "1.1"

  override def unmanagedClasspath =
    super.unmanagedClasspath +++ buildCompilerJar

  override def filterScalaJars = false

  override def libraryDependencies = Set(
	"org.scala-lang" % "scala-compiler" % scalaVersion % "compile->default",
  
    "net.liftweb" % ("lift-webkit_" + scalaVersion) % liftVersion % "compile->default",
    "net.liftweb" % ("lift-mapper_" + scalaVersion) % liftVersion % "compile->default",

    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default",
    "junit" % "junit" % "4.5" % "test->default",
    "ch.qos.logback" % "logback-classic" % "0.9.26",
    //"org.scala-tools.testing" %% "specs" % "1.6.6" % "test->default",
    
    "net.liftweb" % ("lift-scalate_" + scalaVersion) % liftVersion % "compile->default",
//    "org.fusesource.scalate" % "scalate-wikitext" % scalateVersion % "compile->default",
//    "org.fusesource.scalate" % "scalate-page" % scalateVersion % "compile->default",
    "org.fusesource.scalamd" % "scalamd" % "1.4" % "compile->default"
) ++ super.libraryDependencies
}