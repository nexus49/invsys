package basic

import org.scalatest.junit.ShouldMatchersForJUnit
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
  val mongoCustomersColl = mongoDb("customers")
  val mongoOrdersColl = mongoDb("orders")

  @Before
  def initialize() {
    // clear collection
    for (x <- mongoColl.find)  mongoColl -= x 
    for (x <- mongoCustomersColl.find)  mongoCustomersColl -= x 
    for (x <- mongoOrdersColl.find)  mongoOrdersColl -= x 
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

  @Test // Verify clean up works
  def shouldVerifyEmptyColl() {
    mongoColl.find.size should be(0)
    mongoCustomersColl.find.size should be(0)
    mongoOrdersColl.find.size should be(0)
  }

  @Test
  def shouldVerifyInsert() {
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
  def shouldVerifyInsertWithEmbeddedChild {
    //Insert TestObject
    mongoColl += MongoDBObject(
      "id" -> "1",
      "type" -> "testObject",
      "size" -> 3.14,
      "name" -> "testName",
      "child" -> MongoDBObject("id" -> "2", "name" -> "child", "childchild" -> MongoDBObject("id" -> "3", "name" -> "childchild")))

    //create query
    val query = new BasicDBObject
    query.put("type", "testObject")

    //get result
    val result: DBObject = mongoColl.findOne(query).get

    // use result
    logger.info("found %s".format(result))
    val child: DBObject = result("child").asInstanceOf[DBObject]
    val childchild: DBObject = child("childchild").asInstanceOf[DBObject]
    val name = result("name")
    result("name") should be("testName")
    child("name") should be("child")
    childchild("name") should be("childchild")
  }

  @Test
  def shouldVerifyInsertWithReferencedChild() {
    //Insert TestObject
    val customer: DBObject = MongoDBObject(
      "id" -> "1",
      "firstname" -> "Bastian",
      "lastname" -> "Mueller")
    mongoCustomersColl += customer

    logger.info(mongoDb.underlying)
    logger.info(customer)

    val order: DBObject = MongoDBObject(
      "ordernumber" -> "1",
      "order_creation" -> new Date,
      "customer" -> customer._id)
    mongoOrdersColl += order

    //create query for customer
    val custQuery = new BasicDBObject
    custQuery.put("id", "1")
    val customerResult: DBObject = mongoCustomersColl.findOne(custQuery).get
    logger.info(customerResult)
    
    customerResult("firstname") should be ("Bastian")
    
    //create query for the customers orders
    val ordersQuery = new BasicDBObject
    ordersQuery.put("customer", customerResult._id)
    val ordersResult: DBObject = mongoOrdersColl.findOne(ordersQuery).get
    logger.info(ordersQuery)
    
    ordersResult("ordernumber") should be ("1")

    //select order separately
    val orderQuery = new BasicDBObject();
    orderQuery.put("ordernumber", "1")
    val orderResult: DBObject = mongoOrdersColl.findOne(orderQuery).get
    logger.info(orderResult)
    
    orderResult("ordernumber") should be ("1")
  }
}
