package basic

import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitSuite
import scala.collection.mutable.ListBuffer
import org.junit.Assert._
import org.junit.Test
import org.junit.Before
import java.util.Date
import com.mongodb._
import org.bson.types._

import com.mongodb.casbah.Imports._
import com.mongodb._
class CasbahTest extends AssertionsForJUnit with ShouldMatchersForJUnit {

  @Before
  def initialize() {
  }

  @Test
  def verifyEasy() {
    val testCollection = MongoConnection()("test")("casbah_test")

    // syntax option 1
    val newObj = MongoDBObject("foo" -> "bar",
      "x" -> "y",
      "pie" -> 3.14,
      "spam" -> "eggs")
    testCollection += newObj
      
    // syntax option 2
    val builder = MongoDBObject.newBuilder
    builder += "author" -> "author"
    builder += "msg" -> "msg"
    testCollection += builder.result.asDBObject
  }

}
