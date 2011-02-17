package pbc.model

import com.mongodb.casbah.{MongoCollection, MongoCursorBase}
import com.mongodb.DBObject
import com.mongodb.BasicDBObject
import scala.collection.mutable
import scala.collection.mutable.MutableList
import scala.collection.mutable.MutableList
import scala.collection.mutable.ListBuffer

trait Dao  {
	def collection:MongoCollection
	
	def findAllDBObjects[T]:MongoCursorBase[DBObject]={
		return collection.find
	}
	
	def findAllTemplated:ListBuffer[Templated]={
		val itr = collection.find
		val result = new ListBuffer[Templated]
		while (itr.hasNext)
		{
			val item = itr.next
			
			result += create(item)
		}
		return result
	}
	
	def findFirstById[T](id:String):DBObject
	={
		val query = new BasicDBObject
		query.put("_id",id)
		return collection.findOne(query).get
	}
	
	def create(dbObject:DBObject):Templated
}