package pbc.snippet

import scala.collection.mutable.ListBuffer
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
import pbc.model.{Inventory, Templated, Template}

class InventorySnippet extends StatefulSnippet with Loggable {
  val dispatch: DispatchIt = {
    case "edit" => edit _
    case "list" => list _
    case "delete" => delete _
  }

  def list(xhtml: NodeSeq): NodeSeq = {

    val allTemplates: List[Template] = Template.findAll
    allTemplates.flatMap(template => {
      bind("a", xhtml,
        "name" -> template.name,
        "inventory" -> Inventory.findAll(template).flatMap(inv =>
          bind("inv", chooseTemplate("inv", "list", xhtml), "id" -> inv.id.toString, "name" -> inv.get("Name").asInstanceOf[String]
        )))})
  }

  def edit(xhtml: NodeSeq): NodeSeq = {
    null
  }

  def delete(xhtml: NodeSeq): NodeSeq = {
    null
  }
}