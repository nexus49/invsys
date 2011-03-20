package pbc.model
import com.mongodb.casbah.Imports._

// Generic inventory type that is templated.
class Inventory(val template: Template) extends Templated {
}

object Inventory extends TemplatedDao
{
	override def fac(dbObject: DBObject, template:Template): Templated = {
			new Inventory(template) setup (dbObject._id.get, values(dbObject))
	}	
}