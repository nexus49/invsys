package pbc.schema

import org.squeryl.dsl.{ OneToMany, ManyToOne }
import net.liftweb.record.{ MetaRecord, Record }
import org.squeryl.Schema
import java.sql.Timestamp
import java.util.Date
import org.squeryl.annotations.Column
import org.squeryl.PrimitiveTypeMode._
import net.liftweb.squerylrecord.KeyedRecord
import net.liftweb.record.field._
import org.squeryl.Query

class Author private () extends Record[Author] with KeyedRecord[Long] {
  def meta = Author

  @Column(name = "id")
  val idField = new LongField(this, 100)
  val name = new StringField(this, "")
  val age = new OptionalIntField(this)
  val birthday = new OptionalDateTimeField(this)

  lazy val books: OneToMany[Book] = Library.authorsToBooks.left(this)

  override def toString = "Author.createRecord.idField(" + idField.value + ", " + name.value + ", " + age.value + ")"
}

object Author extends Author with MetaRecord[Author]

trait Genre extends Enumeration {
  type Genre = Value
  val SciFi = Value(1, "Sci-Fi")
  val Boring = Value(2, "Boring")
  val GetRichQuickScam = Value(3, "GetRichQuickScam")
  val Novel = Value(4, "Novel")
  val Culinary = Value(5, "Culinary")
}

object Genre extends Genre

class Book private () extends Record[Book] with KeyedRecord[Long] {
  def meta = Book

  @Column(name = "id")
  val idField = new LongField(this, 100)
  val name = new StringField(this, "")
  val publishedInYear = new IntField(this, 1990)

  val publisherId = new LongField(this, 0)

  val authorId = new LongField(this, 1234)

  val genre = new EnumField[Book, Genre](this, Genre)

  val secondaryGenre = new OptionalEnumField(this, Genre)

  lazy val author: ManyToOne[Author] = Library.authorsToBooks.right(this)
  lazy val publisher: ManyToOne[Publisher] = Library.publishersToBooks.right(this)
}

object Book extends Book with MetaRecord[Book]

class Publisher private () extends Record[Publisher] with KeyedRecord[Long] {
  def meta = Publisher

  @Column(name = "id")
  val idField = new LongField(this, 1)
  val name = new StringField(this, "")

  lazy val books: OneToMany[Book] = Library.publishersToBooks.left(this)
}

object Publisher extends Publisher with MetaRecord[Publisher]

object Library extends Schema {

  val authors = table[Author]
  val books = table[Book]
  val publishers = table[Publisher]

  val authorsToBooks = oneToManyRelation(authors, books).via((s, c) => s.id === c.idField.is)
  val publishersToBooks = oneToManyRelation(publishers, books).via((s, c) => s.id === c.idField.is)

  // this is a test schema, we can expose the power tools ! :
  override def drop = super.drop
}
