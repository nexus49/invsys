package pbc.model
import pbc.db.CollectionFactory
import com.mongodb.casbah.Imports._

// Generic inventory type that is templated.
class Inventory(val template: Template) extends Templated {

}

/*
object Inventory extends Inventory(null){
  //def create(dbObject: DBObject, template: Template): Templated = { return new InventoryDao(template).create(dbObject) }
}
*/
/*
//First approach of a Factorylike Dao implementation. not sure if that is the best way to go.
class InventoryDao(val template: Template) extends Dao {
  override def collection = CollectionFactory.getCollection(template.collectionName)

  override def create(dbObject:DBObject):Templated =
    {
      val inv = new Inventory(template)
      inv.setup(dbObject)
      return inv
    }
}*/
