package pbc.db

import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.MongoCollection

object CollectionFactory {
  val mongoCon = MongoConnection()
  val mongoDb = mongoCon("invsys")
  
  val idColumn = "_id"

  def getCollection(name: String):MongoCollection = {
    return mongoDb(name)
  }
}