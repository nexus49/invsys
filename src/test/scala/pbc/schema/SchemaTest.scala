package pbc.schema

import net.liftweb.common.{Loggable, Empty}
import scala.tools.nsc.util.trace
import bootstrap.liftweb.Boot
import net.liftweb.util.Props
import java.sql.SQLException
import org.squeryl.adapters.H2Adapter
import net.liftweb.squerylrecord.SquerylRecord
import net.liftweb.mapper.{ StandardDBVendor, DefaultConnectionIdentifier, DB }
import org.junit.Before
import org.scalatest.junit.{AssertionsForJUnit, ShouldMatchersForJUnit}
import org.junit.Test
import org.squeryl.{ SessionFactory, Session }

class SchemaTest extends AssertionsForJUnit with ShouldMatchersForJUnit with Loggable {

  import org.squeryl.PrimitiveTypeMode._

  @Before
  def setup() {
    DB.defineConnectionManager(DefaultConnectionIdentifier,
      new StandardDBVendor("org.h2.Driver", "jdbc:h2:test", Empty, Empty))

    SquerylRecord.init(() => new H2Adapter)
    DB.use(DefaultConnectionIdentifier) { conn =>
      {
        Library.drop
        Library.create
      }
    }
  }

  @Test
  def verifySelect() {
    DB.use(DefaultConnectionIdentifier) { conn =>
      {
        val author: Author = Author.createRecord
        author.name.set("Jim Carry")
        Library.authors.insert(author)

        val newAuthor = from(Library.authors)(b => select(b)).single
        logger.info("Selected authors: %s".format(newAuthor.name.value))
        newAuthor.name.value should be ("Jim Carry")
      }
    }
  }
}