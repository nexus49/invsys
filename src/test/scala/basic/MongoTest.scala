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
import net.liftweb.mongodb._
import com.mongodb._

class MongoTest extends AssertionsForJUnit with ShouldMatchersForJUnit {

  var sb: StringBuilder = _
  var lb: ListBuffer[String] = _

  @Before def initialize() {
    sb = new StringBuilder("ScalaTest is ")
    lb = new ListBuffer[String]
  }

  @Test def verifyEasy() { 
	  	MongoDB.defineDb(DefaultMongoIdentifier, MongoAddress(MongoHost("localhost", 27017), "test_direct"))
	  	
	  	
	  	MongoDB.use(DefaultMongoIdentifier) ( db => {
  val doc = new BasicDBObject
  doc.put("name", "MongoDB")
  doc.put("type", "database")
  doc.put("count", 1)
  val coll = db.getCollection("testCollection")
  // save the doc to the db
  coll.save(doc)
})

  }

	

}