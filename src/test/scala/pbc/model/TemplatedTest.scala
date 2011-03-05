package pbc.model
import org.scalatest.junit._
import org.junit._
import net.liftweb.common.Loggable

import com.mongodb.casbah.commons.MongoDBObject
import java.util.Date
import pbc.db.CollectionFactory

class TemplateTests extends JUnitSuite with ShouldMatchersForJUnit with Loggable {
  val collFac = CollectionFactory

  val template = new Template(List[String]("id", "po_number","serialnumber","department_id","purchase_date"), "inventory")
  val newObj = MongoDBObject(
    "id" -> "1234",
    "po_number" -> "12323",
    "serialnumber" -> "QE-WE-WQ-QWE",
    "department_id" -> "1",
    "purchase_date" -> new Date)
  @Before
  def initialize() {
    for (x <- collFac.getCollection("inventory").find) collFac.getCollection("inventory") -= x
    collFac.getCollection("inventory") += newObj
  }

  @Test
  def testTemplatedSetup() {
    val inv = new Inventory(template)
    inv.setup(newObj)
    inv.valueMap.size should be (6)
  }
  
  @Test
  def testTemplatedSet() {
    val inv = new Inventory(template)
    inv.set("po_number", "11111")
    evaluating { inv.set("op_number", "11111") } should produce [IllegalArgumentException]
  }
  
  @Test
  def testTemplatedGet() {
    val inv = new Inventory(template)
    inv.get("po_number") should be (null)
    evaluating { inv.get("op_number") } should produce [IllegalArgumentException]
  }

}