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
import net.liftweb.mongodb._
import com.mongodb._
import org.bson.types._

class MongoTest extends AssertionsForJUnit with ShouldMatchersForJUnit {

  var sb: StringBuilder = _
  var lb: ListBuffer[String] = _

  @Before
  def initialize() {
    MongoDB.defineDb(DefaultMongoIdentifier, MongoAddress(MongoHost("localhost", 27017), "test"))

  }

  @Test
  def verifyEasy() {
    MongoDB.use(DefaultMongoIdentifier)(db => {
      val doc = new BasicDBObject
      doc.put("name", "MongoDB")
      doc.put("type", "database")
      doc.put("count", 1)
      val coll = db.getCollection("testCollection")
      // save the doc to the db
      coll.save(doc)
    })

    MongoDB.useCollection("testCollection")(coll => {
      val doc = new BasicDBObject
      doc.put("name", "MongoDB")
      doc.put("type", "database")
      doc.put("hans", "dieter")
      doc.put("count", 1)
      // save the doc to the db
      coll.save(doc)
    })
  }

  @Test
  def verifyMongoDocument() {
    def date(s: String) = Person.formats.dateFormat.parse(s).get
    val p = Person(
      ObjectId.get.toString,
      "joe",
      27,
      Address("Bulevard", "Helsinki"),
      List(Child("Mary", 5, Some(date("2004-09-04T18:06:22.000Z"))), Child("Mazy", 3, None)))
    
    p.save
  }
}

case class Address(street: String, city: String)

case class Child(name: String, age: Int, birthdate: Option[Date])
case class Person(_id: String, name: String, age: Int, address: Address, children: List[Child])
  extends MongoDocument[Person] {
  def meta = Person
}
object Person extends MongoDocumentMeta[Person] {
  override def mongoIdentifier = DefaultMongoIdentifier
  override def collectionName = "mypersons"
}