package pbc.model
import com.mongodb.DBObject

trait MongoBased {
	// create method to initialize a mongo based object
	def create(dbObject:DBObject)
}