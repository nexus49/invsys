package pbc.model
import pbc.db.CollectionFactory
import com.mongodb.casbah.Imports._

// Generic inventory type that is templated.
class Inventory(val template: Template) extends Templated {
}

object Inventory extends TemplatedDao
{
	override def fac(dbObject: DBObject, template:Template): Templated = {
			new Inventory(template) setup (values(dbObject))
	}	
}