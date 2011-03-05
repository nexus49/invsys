package pbc.model
import org.scalatest.junit._
import org.junit._
import net.liftweb.common.Loggable
import com.mongodb.casbah.MongoConnection

class MongoBasedTest extends JUnitSuite with ShouldMatchersForJUnit with Loggable {

  val template = new Template(List[String]("_id", "po_number", "serialnumber", "department_id", "purchase_date"), "inventory")

    @Before
  def initialize() {
    // clear collection
	val mongoColl = MongoConnection()("test")("inventory")
    for (x <- mongoColl.find)  mongoColl -= x  
  }
  
  @Test
  def testTemplatedGet() {
    val inv = new Inventory(template)
    inv.set("_id", "123")
    inv.set("serialnumber", "123123123")
    inv.save(inv)
    
    val item = inv.findFirstById("123").asInstanceOf[Inventory]
    item.get("serialnumber") should be("123123123")
  }
}