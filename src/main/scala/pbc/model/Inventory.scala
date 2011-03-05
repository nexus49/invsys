package pbc.model
import pbc.db.CollectionFactory
import com.mongodb.casbah.Imports._

// Generic inventory type that is templated.
class Inventory(val template: Template) extends MongoBased with Templated with TemplatedDao{
	override def collection:MongoCollection = MongoConnection()("test")(template.collectionName)
	override def create(dbObject:DBObject) { setup(dbObject) }
	override def fac():MongoBased = {new Inventory(template)}
}