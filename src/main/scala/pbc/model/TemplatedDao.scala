package pbc.model

import com.mongodb.casbah.{ MongoCollection, MongoCursorBase }
import com.mongodb.casbah.Imports._
import com.mongodb.DBObject
import scala.collection.mutable
import pbc.db.CollectionFactory

// Adds dao functionality to a object
trait TemplatedDao {
  // Factory method to create a new instance
  def fac(dbObject: DBObject, template: Template): Templated

  def findAll(template: Template): List[Templated] = {
    val collection = CollectionFactory.getCollection(template.collectionName)
    val values = new mutable.ListBuffer[Templated]
    for (result <- collection.find) {
      val item = fac(result, template)
      values += (item)
    }
    return values.toList
  }

  def findFirstById[T](id: String, template: Template): Templated = {
    val collection = CollectionFactory.getCollection(template.collectionName)
    val query = new BasicDBObject
    query.put("_id", id)
    val result = collection.findOne(query).get
    val item = fac(result, template)
    return item
  }

  def save(item: Templated) {
    val collection = CollectionFactory.getCollection(item.template.collectionName)
    val mongoItem = toDBObject(item)
    collection += mongoItem
  }

  // Creates a DBOBject from the template
  private def toDBObject(item: Templated): DBObject =
    {
      val builder = MongoDBObject.newBuilder
      val values = item.valueMap
      values foreach {
        case (key, value) =>
          if (value.isInstanceOf[Templated]) { builder += key -> toDBObject(value.asInstanceOf[Templated]) }
          else { builder += key -> value }
      }
      builder.result.asDBObject
    }

  def values(dbObject: DBObject): Map[String, Object] =
    {
      val valueMap = mutable.Map.empty[String, Object]
      val itr = dbObject.keySet.iterator
      while (itr.hasNext) {
        val key: String = itr.next
        valueMap += (key -> dbObject(key))
      }
      return valueMap.toMap
    }
}