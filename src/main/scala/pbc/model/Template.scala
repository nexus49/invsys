package pbc.model
import scala.collection.mutable.HashMap
import com.mongodb.casbah.MongoCollection
import pbc.db.CollectionFactory
import com.mongodb.DBObject
import com.mongodb.casbah.Imports._
import scala.collection.mutable
import net.liftweb.common.Loggable
// Simple class to hold a set of keys that template
// We should enrich that class to hold types for the attributes.
class Template(var id: ObjectId, var name: String, var collectionName: String, val attributes: List[String]) {
  override def toString(): String = { "Template(%s,%s,%s,%s)".format(id, name, collectionName, attributes.toString) }
}
object Template extends Loggable {
  val nameColumn = "name"
  val collectionColumn = "collectionName"
  val attributeColumn = "attributes"
  val collection = CollectionFactory.getCollection("templates")

  def findById(id: ObjectId): Template = {
    val query = new BasicDBObject
    val result = collection.findOneByID(id).get
    val tmplName: String = result.get(nameColumn).asInstanceOf[String]
    val tmplCollectionName: String = result.get(collectionColumn).asInstanceOf[String]
    val tmplAttributes: List[String] = toList(result(attributeColumn).asInstanceOf[BasicDBList]) { _.asInstanceOf[String] }
    new Template(result._id.get, tmplName, tmplCollectionName, tmplAttributes)
  }

  def findByName(name: String): Template = {
    val query = new BasicDBObject
    query.put(nameColumn, name)
    val result = collection.findOne(query).get
    val tmplName: String = result(nameColumn).asInstanceOf
    val tmplCollectionName: String = result(collectionColumn).asInstanceOf
    val tmplAttributes: List[String] = toList(result(attributeColumn).asInstanceOf[BasicDBList]) { _.asInstanceOf[String] }
    new Template(result._id.get, tmplName, tmplCollectionName, tmplAttributes)
  }

  def findAll(): List[Template] = {
    val values = new mutable.ListBuffer[Template]
    val queryResult = collection.find
    while (queryResult.hasNext) {
      val result = queryResult.next
      val name: String = result(nameColumn).asInstanceOf[String]
      val collectionName: String = result(collectionColumn).asInstanceOf[String]
      val attributes = toList(result(attributeColumn).asInstanceOf[BasicDBList]) { _.asInstanceOf[String] }
      values += (new Template(result._id.get, name, collectionName, attributes))
    }
    values.toList
  }

  def save(template: Template) {
    val mongoItem = toDBObject(template)
    if (template.id == null) {
      val result = collection.save(mongoItem)
      template.id = mongoItem._id.get
      logger.info("inserted template " + result)
    } else {
      val result = collection.update(MongoDBObject.apply(CollectionFactory.idColumn -> template.id), mongoItem)
      logger.info("updated template " + result)
    }
  }
  def delete(id: ObjectId) {
    val result = collection.remove(MongoDBObject.apply(CollectionFactory.idColumn -> id))
    logger.info("deleted template " + result)
  }

  private def toDBObject(template: Template): DBObject = {
    val builder = MongoDBObject.newBuilder
    builder += nameColumn -> template.name
    builder += collectionColumn -> template.collectionName
    builder += attributeColumn -> template.attributes
    builder.result.asDBObject
  }

  def toList[T](list: BasicDBList)(closure: Object => T): List[T] = {
    list.map(closure(_)).toList
  }

}