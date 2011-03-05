package pbc.model
import com.mongodb.casbah.Imports._
import scala.collection.mutable

trait MongoBased {
	// create method to initialize a mongo based object
	def create(dbObject:DBObject)
}
object MongoBased
{
	def values(dbObject:DBObject):Map[String,Object]=
	{
		val valueMap = mutable.Map.empty[String,Object]
		val itr = dbObject.keySet.iterator
		while(itr.hasNext)
		{
			val key:String = itr.next 
			valueMap += (key -> dbObject(key))
		}
		
		return valueMap.toMap
	}
}