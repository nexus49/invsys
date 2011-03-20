package pbc.model

import com.mongodb.casbah.{MongoCollection, MongoCursorBase}
import com.mongodb.casbah.Imports._
import com.mongodb.DBObject
import scala.collection.mutable
import pbc.db.CollectionFactory
import net.liftweb.common.Loggable

// Adds dao functionality to a object
trait TemplatedDao extends Loggable {
  // Factory method to create a new instance
  def fac(dbObject: DBObject, template: Template): Templated

  def findAll(): List[Templated] = {
    val templates = Template.findAll
    val values = new mutable.ListBuffer[Templated]

    templates.foreach(t => {
      logger.info(t.name)
      val collection = CollectionFactory.getCollection(t.collectionName)

      for (result <- collection.find) {
        val item = fac(result, t)
        values += (item)
      }

    })
    return values.toList
  }

  def findAll(template: Template): List[Templated] = {
    val collection = CollectionFactory.getCollection(template.collectionName)
    val values = new mutable.ListBuffer[Templated]
    val query = MongoDBObject.apply(CollectionFactory.templateIdColumn -> template.id)
    val rslt = collection.find(query)
    while(rslt.hasNext)
    {
      val item = fac(rslt.next, template)
      values += (item)
    }
      return values.toList
  }

  def findFirstById[T](id: ObjectId, template: Template): Templated = {
    val collection = CollectionFactory.getCollection(template.collectionName)
    val result = collection.findOneByID(id)

    if (!result.isEmpty) {
      val item = fac(result.get, template)
      return item
    }
    else {
      return null
    }
  }

  def save(item: Templated) {
    val collection = CollectionFactory.getCollection(item.template.collectionName)
    val mongoItem = toDBObject(item)
    if (item.id == null) {
      val result = collection.save(mongoItem)
      item.id = mongoItem._id.get
      logger.info("inserted Inventory " + result)
    } else {
      val result = collection.update(MongoDBObject.apply(CollectionFactory.idColumn -> item.id), mongoItem)
      logger.info("updated Inventory " + result)
    }

    collection += mongoItem
  }

  // Creates a DBOBject from the template
  private def toDBObject(item: Templated): DBObject = {
    val builder = MongoDBObject.newBuilder
    val values = item.valueMap
    values foreach {
      case (key, value) =>
        if (value.isInstanceOf[Templated]) {
          builder += key -> toDBObject(value.asInstanceOf[Templated])
        }
        else {
          builder += key -> value
        }
    }
    builder += CollectionFactory.templateIdColumn -> item.template.id
    builder.result.asDBObject
  }

  def values(dbObject: DBObject): Map[String, Object] = {
    val valueMap = mutable.Map.empty[String, Object]
    val itr = dbObject.keySet.iterator
    while (itr.hasNext) {
      val key: String = itr.next
      valueMap += (key -> dbObject(key))
    }
    return valueMap.toMap
  }
}