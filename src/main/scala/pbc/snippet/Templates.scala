package pbc.snippet

import scala.collection.mutable.ListBuffer
import pbc.model.Template
import scala.xml.NodeSeq
import net.liftweb.common.Loggable
import scala.xml.Text
import net.liftweb.http.RequestVar
import _root_.net.liftweb._
import common.{ Box }
import http._
import S._
import util._
import Helpers._
import org.bson.types.ObjectId

object templateVar extends RequestVar[Template](null)

class Templates extends StatefulSnippet with Loggable {

  val dispatch: DispatchIt = {
    case "add" => add _
    case "listTemplates" => listTemplates _
  }

  def template() = { templateVar.is }

  def listTemplates(xhtml: NodeSeq): NodeSeq = {
    val templates: List[Template] = Template.findAll

    def test() {}
    templates.flatMap(template => bind("template", xhtml,
      "name" -> template.name,
      "collectionName" -> template.collectionName,
      "attributes" -> template.attributes.reduceLeft[String] { (string, n) => string + "," + n },
      "edit" -%> SHtml.link("edit", () => templateVar(template), Text("edit"))))
  }

  def add(xhtml: NodeSeq): NodeSeq = {
    var id: String = if (template != null) template.id.getOrElse("").toString else ""
    var name = if (template != null) template.name else ""
    var attributes = if (template != null) template.attributes.reduceLeft[String] { (string, n) => string + "," + n } else ""
    val isAdd = (template == null)

    def receiveSubmit() {
      if (isAdd) {
        Template.save(new Template(None, name, "Inventory", attributes.split(",").toList))
      } else {
        val template = Template.findById(new ObjectId(id))
        template.name = name
        template.attributes.drop(0)
        template.attributes ++ attributes.split(",").toList
        Template.save(template)
      }

      S.redirectTo("/template/list")
    }

    bind("entry", xhtml,
      "name" -> SHtml.text(name, name = _),
      "hidden" -> SHtml.hidden(isAdd.toString),
      "id" -> SHtml.hidden(id.toString),
      "attributes" -> SHtml.text(attributes, attributes = _),
      "submit" -> SHtml.submit(if (isAdd) "Add" else "Update", receiveSubmit))
  }

}