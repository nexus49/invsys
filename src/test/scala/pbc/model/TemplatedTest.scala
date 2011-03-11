package pbc.model
import org.scalatest.junit._
import org.junit._
import net.liftweb.common.Loggable

import com.mongodb.casbah.commons.MongoDBObject
import java.util.Date
import pbc.db.CollectionFactory

class TemplateTests extends JUnitSuite with ShouldMatchersForJUnit with Loggable {
  val collFac = CollectionFactory

  val template = new Template(None,"hardware", "inventory", List[String]("_id", "po_number","serialnumber","department_id","purchase_date"))
  val newObj = MongoDBObject(
    "_id" -> "1234",
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
    inv.setup(Inventory.values(newObj))
    inv.valueMap.size should be (5)
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
  
  @Test
  def testSaveNewInventory() {
    val inv = new Inventory(template)
    inv.set("_id", "123")
    inv.set("serialnumber", "123123123")
    Inventory.save(inv)
    
    val item = Inventory.findFirstById("123", template).asInstanceOf[Inventory]
    item.get("serialnumber") should be("123123123")
  }
  
  @Test
  def testSaveUpdatedInventory() {
    //val item = inv.findFirstById("123").asInstanceOf[Inventory]
    //item.get("serialnumber") should be("123123123")
    
  }

}