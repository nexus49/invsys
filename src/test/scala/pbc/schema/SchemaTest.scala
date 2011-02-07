package pbc.schema

import net.liftweb.common.Loggable
import scala.tools.nsc.util.trace
import net.liftweb.common.Empty
import bootstrap.liftweb.Boot
import net.liftweb.util.Props
import java.sql.SQLException
import org.squeryl.adapters.H2Adapter
import net.liftweb.squerylrecord.SquerylRecord
import net.liftweb.mapper.{ StandardDBVendor, DefaultConnectionIdentifier, DB }
import org.junit.Before
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.ShouldMatchersForJUnit
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

        val authors = from(Library.authors)(b => select(b))
        logger.info("Selected authors:")
        logger.info(authors.single.name.value) 
        
        authors.single.name.value should be ("Jim Carry")
      }
    }
  }
}