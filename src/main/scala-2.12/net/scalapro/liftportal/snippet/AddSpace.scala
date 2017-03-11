package net.scalapro.liftportal.snippet

import net.liftweb.common.Full
import net.liftweb.http.js.JsCmd
import net.liftweb.http.{RequestVar, S, SHtml}
import net.liftweb.util.{CssSel, PassThru}
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.tables.Space
import net.scalapro.liftportal.util.{DB, Tags}

import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import net.liftweb.http.js.JsCmds._


object t extends RequestVar(("","0"))
object AddSpace  {

  private var id = ("","0")

  def render: CssSel = {

    S.param("t") match {
      case Full(x) => id = ("t", x)
      case _ =>
    }

    S.param("c") match {
      case Full(x) => id = ("c", x)
      case _ =>
    }


    if(id._2 == "0") return "*" #> ""

    t.set(id)

      "#spaces *" #> net.liftweb.http.Templates("cms"::"_space-table"::Nil) &
      ".title *" #> "Add a Space" &
        "type=submit" #> SHtml.ajaxButton(<span><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add new space</span>, process _)

  }

  private def process(): JsCmd = {
    val q = id match {
      case ("t", x) => Space.table += Space(None, None, Some(x.toInt), None)
      case ("c", x) => Space.table += Space(None, Some(x.toInt), None, None)
      case _ => return Noop
    }

    val db = DB.getDatabase
    try {
      Await.result(
        db.run(q)
        , Duration.Inf)
    }
    finally {
      db.close
    }
    t.set(id)
    net.liftweb.http.Templates("cms"::"_space-table"::Nil).map(ns => SetHtml("spaces", ns)) openOr Noop
  }

}
