package bootstrap.liftweb

import _root_.net.liftweb.common._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("pbc")

    LiftRules.determineContentType = {
      case (Full(Req("template" :: "list" :: Nil, _, GetRequest)), _) => "text/html; charset=utf-8"
      case (Full(Req("tour" :: "edit" :: "view" :: Nil, _, GetRequest)), _) =>
        "text/html; charset=utf-8"
      case (Full(Req("tour" :: "add" :: "view" :: Nil, _, GetRequest)), _) =>
        "text/html; charset=utf-8"
      case _ => "text/html; charset=utf-8"
    }
    // Build SiteMap
    val homeEntries = Menu(Loc("Home", List("index"), "Home")) :: Nil
   
    val templatesMenueEntries: List[Menu] = List(
      Menu(S ? "Template") / "template" / "list" submenus(
    		Menu(S ? "Add Template") / "template" / "edit", 
    		Menu(S ? "Delete Template") / "template" / "delete"
      		)
    	)

    val entries = homeEntries ::: templatesMenueEntries

    LiftRules.setSiteMap(SiteMap(entries: _*))
  }
}

