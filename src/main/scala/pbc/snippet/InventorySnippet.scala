package pbc.snippet
import net.liftweb.http.StatefulSnippet
import net.liftweb.common.Loggable
import scala.xml.NodeSeq

class InventorySnippet extends StatefulSnippet with Loggable {
	val dispatch: DispatchIt = {
    case "edit" => edit _
    case "list" => list _
    case "delete" => delete _
  }

	def list(xhtml: NodeSeq): NodeSeq = {
		null
	}
	
	def edit(xhtml: NodeSeq): NodeSeq = {
		null
	}
	
	def delete(xhtml: NodeSeq): NodeSeq = {
		null
	}
}