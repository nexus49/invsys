package pbc.model
import net.liftweb.common.Loggable
import java.lang.IllegalArgumentException
import scala.collection.mutable
import org.bson.types.ObjectId


trait Templated {
	var id:ObjectId = null
	def template:Template
	val valueMap = mutable.Map.empty[String,Object]
	
	// sets a attribute that is part of the value map
	def set (key:String,value:Object)
	{
		if (template.attributes .contains(key)) { valueMap += (key -> value) }
		else throw new IllegalArgumentException("element does not exist")
	}
	
	// returns a value that is part of the valuesMap
	def get (key:String):Object =
	{
		if (!template.attributes .contains(key)) { throw new IllegalArgumentException("element does not exist") }
		if(valueMap .contains(key)) return valueMap(key) 
		else return null
	}
	
	// Initializes a templated object with key values
	def setup(id:ObjectId, values:Map[String,Object]):Templated
	={
    this.id = id
		values foreach {
			case (key, value) => valueMap += (key -> value)
		}
		this
	}
}