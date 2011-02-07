package bootstrap.liftweb

import _root_.net.liftweb.common._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import net.liftweb.mapper.{ DB, DefaultConnectionIdentifier, StandardDBVendor }
import net.liftweb.squerylrecord.SquerylRecord
import org.squeryl.adapters.H2Adapter

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    println(Props.get("test.me"))
    if (!DB.jndiJdbcConnAvailable_?)
      DB.defineConnectionManager(DefaultConnectionIdentifier,
        new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
          Props.get("db.url") openOr "jdbc:h2:test",
          Props.get("db.user"), Props.get("db.password")))

    SquerylRecord.init(() => new H2Adapter)

    // where to search snippet
    LiftRules.addToPackages("pbc")

    // Build SiteMap
    val entries = Menu(Loc("Home", List("index"), "Home")) :: Nil
    LiftRules.setSiteMap(SiteMap(entries: _*))
  }
}

