package pbc.model
import org.scalatest.junit._
import org.junit._
import net.liftweb.common.Loggable
import com.mongodb.casbah.commons.MongoDBObject
import java.util.Date
import pbc.db.CollectionFactory
import pbc.db.UnitTestCollectionFactory

class TemplateTests extends JUnitSuite with ShouldMatchersForJUnit with Loggable {
  val collFac = CollectionFactory

  var hardwareTmpl:Template = null
  var softwareTmpl:Template = null
  var sampleInvDBObject:Inventory = null

  @Before
  def initialize() {
    for (x <- collFac.getCollection("Inventory").find) collFac.getCollection("Inventory") -= x
    for (x <- collFac.getCollection(Template.collection.name).find) collFac.getCollection(Template.collection.name) -= x

    createDocuments
  }

  @Test
  def testTemplatedSetup() {
    val inv = new Inventory(hardwareTmpl)
    val dbo = collFac.getCollection("Inventory").findOne(MongoDBObject.apply(("Name" -> "Jira")))
    inv.setup(Inventory.values(dbo.get))
    inv.valueMap.size should be(4)
  }

  @Test
  def testTemplatedSet() {
    val inv = new Inventory(hardwareTmpl)
    inv.set("Serial Number", "11111")
    evaluating { inv.set("op_number", "11111") } should produce[IllegalArgumentException]
  }

  @Test
  def testTemplatedGet() {
    val inv = new Inventory(hardwareTmpl)
    inv.get("Serial Number") should be(null)
    evaluating { inv.get("op_number") } should produce[IllegalArgumentException]
  }

  @Test
  def testSaveNewInventory() {
    val inv = new Inventory(hardwareTmpl)
    inv.set("Serial Number", "123123123")
    Inventory.save(inv)
    val id = inv.id

    val item = Inventory.findFirstById(id, hardwareTmpl).asInstanceOf[Inventory]
    item.get("Serial Number") should be("123123123")
  }

  def createDocuments {
    hardwareTmpl = createTemplate("Hardware", "Name,Serial Number,Model")
    softwareTmpl = createTemplate("Software", "Name,License Key,Number of Users")
    
    sampleInvDBObject = createInventory(hardwareTmpl, "Dell Monitor", Map("Serial Number" -> "QWED-QWE", "Model" -> "HP 123"))
    createInventory(hardwareTmpl, "Dell Monitor", Map("Serial Number" -> "QWED-QWE", "Model" -> "HP 123"))

    createInventory(softwareTmpl, "Jira", Map[String, Object]("License Key" -> "123-QWE", "Number of Users" -> Integer.valueOf(100)))
    createInventory(softwareTmpl, "Confluence", Map[String, Object]("License Key" -> "234-QWE", "Number of Users" -> Integer.valueOf(150)))

  }

  def createInventory(template: Template, name: String, attributes: Map[String, Object]): Inventory = {
    val inv = new Inventory(template)
    inv.set("Name", name)
    attributes.foreach(i => inv.set(i._1, i._2))
    Inventory.save(inv)
    inv
  }

  def createTemplate(name: String, attributes: String): Template = {
    val tmp = new Template(null, name, "Inventory", attributes.split(",").toList)
    Template.save(tmp)
    tmp
  }

}