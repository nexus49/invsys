package pbc.model
import scala.collection.mutable.HashMap
import com.mongodb.casbah.MongoCollection
import pbc.db.CollectionFactory
import com.mongodb.DBObject
import com.mongodb.casbah.Imports._
import scala.collection.mutable
// Simple class to hold a set of keys that template
// We should enrich that class to hold types for the attributes.
class Template(val name: String, val collectionName: String, val attributes: List[String]) {
}
object Template {
  val nameColumn = "name"
  val collectionColumn = "collectionName"
  val attributeColumn = "attributes"
  val collection = CollectionFactory.getCollection("templates")

  def findByName(name: String): Template = {
    val query = new BasicDBObject
    query.put(nameColumn, name)
    val result = collection.findOne(query).get
    val tmplName: String = result(nameColumn).asInstanceOf
    val tmplCollectionName: String = result(collectionColumn).asInstanceOf
    val tmplAttributes: List[String] = result(attributeColumn).asInstanceOf
    new Template(tmplName, tmplCollectionName, tmplAttributes)
  }

  def findAll(): List[Template] = {
    val values = new mutable.ListBuffer[Template]
    val queryResult = collection.find
    while (queryResult.hasNext) {
      val result = queryResult.next
      val name: String = result(nameColumn).asInstanceOf[String]
      val collectionName: String = result(collectionColumn).asInstanceOf[String]
      val attributes = List(result(attributeColumn).asInstanceOf[BasicDBList].toArray: _*).map(_.asInstanceOf[String]) 
      values += (new Template(name, collectionName, attributes))
    }
    values.toList
  }

  def save(template: Template) {
    val mongoItem = toDBObject(template)
    collection += mongoItem
  }
  def delete(name: String) {
    val query = new BasicDBObject
    query.put(nameColumn, name)
    val mongoItem = collection.findOne(query).get
    collection -= mongoItem
  }

  private def toDBObject(template: Template): DBObject = {
    val builder = MongoDBObject.newBuilder
    builder += nameColumn -> template.name
    builder += collectionColumn -> template.collectionName
    builder += attributeColumn -> template.attributes
    builder.result.asDBObject
  }

}