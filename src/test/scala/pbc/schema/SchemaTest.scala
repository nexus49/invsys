package pbc.schema

import bootstrap.liftweb.Boot
import net.liftweb.util.Props
import java.sql.SQLException
import org.squeryl.adapters.H2Adapter
import net.liftweb.squerylrecord.SquerylRecord
import net.liftweb.mapper.StandardDBVendor
import net.liftweb.mapper.DefaultConnectionIdentifier
import net.liftweb.mapper.DB
import org.junit.Before
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit.Test

class SchemaTest extends AssertionsForJUnit with ShouldMatchersForJUnit {

  @Before
  def setup() {
	  val boot:Boot = new Boot
	  boot.boot
  }

  @Test
  def verifySelect() {
    import Library._

    val str: String = "test"
    str should be("test")

    val newAuthor = authors.insert(new Author())

    val aId: Long = 0

    //def selectedAuthor = from(authors)(s => where(s.id === id) select (s))

  }
}