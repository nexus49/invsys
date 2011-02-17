package pbc.db

import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.MongoCollection

object CollectionFactory {
  val mongoCon = MongoConnection()
  val mongoDb = mongoCon("test")

  def getCollection(name: String):MongoCollection = {
    return mongoDb(name)
  }
}