package pbc.snippet

import _root_.scala.xml.NodeSeq
import net.liftweb.util.Helpers._
import java.util.Date
import net.liftweb.http.SHtml
import scala.xml.Text

class HelloWorld {
  def howdy(in: NodeSeq): NodeSeq = {
    val x = bind("b", in,
      "templates" -> SHtml.link("template/list", null, Text("Templates")),
      "time" -> new Date().toString)
    x
  }
}

