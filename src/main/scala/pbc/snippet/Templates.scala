package pbc.snippet
import scala.collection.mutable.ListBuffer
import pbc.model.Template
import scala.xml.NodeSeq
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml
import net.liftweb.common.Loggable

class Templates extends Loggable {

  def listTemplates(xhtml: NodeSeq): NodeSeq = {
    val templates: List[Template] = Template.findAll

    templates.flatMap(template => bind("template", xhtml,
      "name" -> template.name,
      "collectionName" -> template.collectionName,
      "attributes" -> template.attributes.reduceLeft[String] { (string, n) =>
        string + "," + n
      }))
  }

  def add(xhtml: NodeSeq): NodeSeq = {
    var name = ""
    var attributes = ""

    def receiveSubmit() {
    	logger.info(name+" "+attributes)
    	Template.save(new Template(name, "Inventory", attributes.split(",").toList))
    }
    
    bind("entry", xhtml,
      "name" -> SHtml.text(name, name = _),
      "attributes" -> SHtml.text(attributes, attributes = _),
      "submit" -> SHtml.submit("Add", receiveSubmit))
  }

}