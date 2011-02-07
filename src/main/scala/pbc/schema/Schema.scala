package pbc.schema

import org.squeryl.Schema
import java.sql.Timestamp
import java.util.Date
import org.squeryl.annotations.Column
import org.squeryl.PrimitiveTypeMode._

class Author(	val id: Long,
				val firstName: String,
				val lastName: String,
				val email: Option[String]) {
  def this() = this(0, "", "", Some(""))
}

// fields can be mutable or immutable 

class Book(	val id: Long,
			var title: String,
			@Column("AUTHOR_ID") // the default 'exact match' policy can be overriden
			var authorId: Long,
			var coAuthorId: Option[Long]) {

  def this() = this(0, "", 0, Some(0L))
}

class Borrowal(val id: Long,
  val bookId: Long,
  val borrowerAccountId: Long,
  val scheduledToReturnOn: Date,
  val returnedOn: Option[Timestamp],
  val numberOfPhonecallsForNonReturn: Int)

object Library extends Schema {

  //When the table name doesn't match the class name, it is specified here :
  val authors = table[Author]
  val books = table[Book]
  val borrowals = table[Borrowal]
  
  def dropTest() = { drop }
	  
  
}
