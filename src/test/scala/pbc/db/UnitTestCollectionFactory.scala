package pbc.db
import com.mongodb.casbah._

object UnitTestCollectionFactory {
  val mongoCon = MongoConnection()
  val mongoDb = mongoCon("test")

  def getCollection(name: String):MongoCollection = {
    return mongoDb(name)
  }
}