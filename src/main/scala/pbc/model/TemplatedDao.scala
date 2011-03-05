package pbc.model

import com.mongodb.casbah.{MongoCollection, MongoCursorBase}
import com.mongodb.casbah.Imports._
import com.mongodb.DBObject
import com.mongodb.BasicDBObject
import scala.collection.mutable.MutableList
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.MutableList
import java.lang.reflect.ParameterizedType

// Adds Dao functionality to a object
trait TemplatedDao {
	// Collection where the items are stored
	def collection:MongoCollection
	// Factory method to create a new instance
	def fac():MongoBased
	
	def findAll():MutableList[MongoBased] 
	={
		val valueMap = new MutableList[MongoBased]
		 for (x <- collection.find)  {
			 val item = fac()
			item.create(x)
			valueMap += (item)
		 } 
		return valueMap
	}
	
	def findFirstById[T](id:String):MongoBased
	={
		val query = new BasicDBObject
		query.put("_id",id)
		val result = collection.findOne(query).get
		val item = fac()
		item.create(result)
		return item
	}	
	
	def save(item:Templated)
	{
		val mongoItem = build(item)
		collection+= mongoItem
	}
	
	// Creates a DBOBject from the template
	private def build(item:Templated):DBObject=
	{
		val builder = MongoDBObject.newBuilder
		val values = item.valueMap
		values foreach {
			case (key, value) => 
				if(value.isInstanceOf[Templated]) { builder += key -> build(value.asInstanceOf[Templated]) }
				else { builder += key -> value }
		}
		builder.result.asDBObject
	}
}