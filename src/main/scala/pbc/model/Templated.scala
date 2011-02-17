package pbc.model
import com.mongodb.casbah.Imports._
import net.liftweb.common.Loggable
import java.lang.IllegalArgumentException
import scala.collection.mutable


trait Templated extends Loggable {
	def template:Template
	val valueMap = mutable.Map.empty[String,Object]
	
	// sets a attribute that is part of the value map
	def set (key:String,value:Object)
	{
		if (template.attributes .contains(key)) { valueMap + (key -> value) }
		else throw new IllegalArgumentException("element does not exist")
	}
	
	// returns a value that is part of the valuesMap
	def get (key:String):Object =
	{
		if (!template.attributes .contains(key)) { throw new IllegalArgumentException("element does not exist") }
		if(valueMap .contains(key)) return valueMap(key) 
		else return null
	}
	
	// I don't link that approach. The Templated trait should be independent from the DBObjects. 
	// We should change that to use a map kind of structure
	def setup(dbObject:DBObject)
	{
		val keyItr = dbObject.keySet.iterator
		while(keyItr.hasNext) 
		{ 
			val key = keyItr.next
			val value = dbObject(key)
			valueMap += (key -> value) 
		}
	}
}