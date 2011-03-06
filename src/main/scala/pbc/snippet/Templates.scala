package pbc.snippet
import scala.collection.mutable.ListBuffer
import pbc.model.Template
import scala.xml.NodeSeq
import net.liftweb.util.Helpers._

class Templates {
  
	def listTemplates(xhtml: NodeSeq): NodeSeq ={
		val templates: List[Template] = Template.findAll
		val t1 = new Template("template1", "abc" ,List("_id", "name", "attributes"))
		val t2 = new Template("template2", "abc" ,List("_id", "name", "attributes"))
		
		templates.flatMap(template => bind("template", xhtml, 
		"name" -> template.name,
		"collectionName" -> template.collectionName ,
		"attributes" -> template.attributes.reduceLeft[String]
		{ 
			(string, n) => string+","+n 
		}))
	}
	
}