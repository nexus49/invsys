package basic

import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitRunner
import org.scalatest.junit.JUnitSuite
import scala.collection.mutable.ListBuffer
import org.junit.Assert._
import org.junit.Test
import org.junit.Before
import java.util.Date
import org.bson.types._
import com.mongodb.casbah.Imports._
import net.liftweb.common.Loggable
import com.mongodb.BasicDBObject

class CasbahTest extends JUnitSuite with ShouldMatchersForJUnit with Loggable {

  val mongoCon = MongoConnection()
  val mongoDb = mongoCon("test")
  val mongoColl = mongoDb("casbah_test")

  @Before
  def initialize() {
    // clear collection
    for (x <- mongoColl.find) {
      logger.info("removed %s".format(x))
      mongoColl -= x
    }
  }

  @Test
  def verifyEasy() {
    // syntax option 1
    val newObj = MongoDBObject(
      "foo" -> "bar",
      "x" -> "y",
      "pie" -> 3.14,
      "spam" -> "eggs")
    mongoColl += newObj

    // syntax option 2
    val builder = MongoDBObject.newBuilder
    builder += "author" -> "author"
    builder += "msg" -> "msg"
    mongoColl += builder.result.asDBObject
  }

  @Test
  // Verify clean up works
  def shouldVerifyEmptyColl() {
    val allElements = mongoColl.find
    allElements.size should be(0)
  }

  @Test
  def shouldVerifyInsertAndSelect() {
    //Insert TestObject
    mongoColl += MongoDBObject(
      "id" -> "1",
      "type" -> "testObject",
      "size" -> 3.14,
      "name" -> "testName")

    //create query
    val query = new BasicDBObject();
    query.put("type", "testObject")

    //get result
    val result: DBObject = mongoColl.findOne(query).get

    // use result
    logger.info("found %s".format(result))
    val name = result("name")
    result("name") should be("testName")
  }
  
  @Test
  def shouldVerifyInsertWithChildAndSelect() {
    //Insert TestObject
    mongoColl += MongoDBObject(
      "id" -> "1",
      "type" -> "testObject",
      "size" -> 3.14,
      "name" -> "testName",
      "child" -> MongoDBObject("id" -> "2", "name" -> "child", "childchild" -> MongoDBObject("id" -> "3", "name" -> "childchild" )))

    //create query
    val query = new BasicDBObject();
    query.put("type", "testObject")

    //get result
    val result: DBObject = mongoColl.findOne(query).get

    // use result
    logger.info("found %s".format(result))
    val child:DBObject = result("child").asInstanceOf[DBObject]
    val childchild:DBObject = child("childchild").asInstanceOf[DBObject]
    val name = result("name")
    result("name") should be("testName")
    child("name") should be ("child")
    childchild("name") should be ("childchild")
  }

}
