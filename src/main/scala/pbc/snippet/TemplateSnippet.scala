package pbc.snippet

import scala.collection.mutable.ListBuffer
import pbc.model.Template
import scala.xml.NodeSeq
import net.liftweb.common.Loggable
import scala.xml.Text
import net.liftweb.http.RequestVar
import _root_.net.liftweb._
import common.{Box}
import http._
import S._
import util._
import Helpers._
import org.bson.types.ObjectId

object templateVar extends RequestVar[Template](null)

class TemplateSnippet extends StatefulSnippet with Loggable {

  val dispatch: DispatchIt = {
    case "edit" => edit _
    case "list" => list _
    case "delete" => delete _
  }

  def template() = { templateVar.is }

  def list(xhtml: NodeSeq): NodeSeq = {
    val templates: List[Template] = Template.findAll

    templates.flatMap(template => bind("template", xhtml,
      "name" -> template.name,
      "collectionName" -> template.collectionName,
      "attributes" -> template.attributes.reduceLeft[String] { (string, n) => string + "," + n },
      "edit" -%> SHtml.link("edit", () => templateVar(template), Text("edit")),
      "delete" -%> SHtml.link("delete", () => templateVar(template), Text("delete"))))
  }

  def edit(xhtml: NodeSeq): NodeSeq = {
    var id: String = if (template != null) template.id.toString else ""
    var name = if (template != null) template.name else ""
    var attributes = if (template != null) template.attributes.reduceLeft[String] { (string, n) => string + "," + n } else ""
    val isAdd = (template == null)

    def receiveSubmit() {
      if (isAdd) {
        Template.save(new Template(null, name, "Inventory", attributes.split(",").toList))
      } else {
        Template.save(new Template(new ObjectId(id), name, "Inventory", attributes.split(",").toList))
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
  def delete(xhtml: NodeSeq): NodeSeq = {
    var id: String = template.id.toString
    var name = if (template != null) template.name else ""
    var attributes = if (template != null) template.attributes.reduceLeft[String] { (string, n) => string + "," + n } else ""

    def deleteTemplate() {
      Template.delete(new ObjectId(id))
      S.redirectTo("/template/list")
    }
    def cancelDeletion() {
      S.redirectTo("/template/list")
    }

    bind("entry", xhtml,
      "name" -> Text(name),
      "id" -> SHtml.hidden(id.toString),
      "attributes" -> Text(attributes),
      "ok" -> SHtml.submit("Delete", deleteTemplate),
      "cancel" -> SHtml.submit("Cancel", cancelDeletion))
  }
}