package pbc

import org.scalatest.junit.ShouldMatchersForJUnit
import org.specs.runner.JUnitSuiteRunner
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitSuite
import scala.collection.mutable.ListBuffer
import org.junit.Assert._
import org.junit.Test
import org.junit.Before

class ScalaTest extends AssertionsForJUnit  with ShouldMatchersForJUnit {

  var sb: StringBuilder = _
  var lb: ListBuffer[String] = _

  @Before def initialize() {
    sb = new StringBuilder("ScalaTest is ")
    lb = new ListBuffer[String]
  }

  @Test def verifyEasy() { // Uses JUnit-style assertions
    sb.append("easy!")
    assertEquals("ScalaTest is easy!", sb.toString)
    assertTrue(lb.isEmpty)
    lb += "sweet"
    try {
      "verbose".charAt(-1)
      fail()
    }
    catch {
      case e: StringIndexOutOfBoundsException => // Expected
    }
  }
  
  @Test def verifyFun() { // Uses ScalaTest matchers
    sb.append("fun!")
    sb.toString should be ("ScalaTest is fun!")
    lb should be ('empty)
    lb += "sweet"
    evaluating { "concise".charAt(-1) } should produce [StringIndexOutOfBoundsException]
  }
}