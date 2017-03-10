package net.scalapro.liftportal.snippet

import net.liftweb.http.js.JsCmd
import net.liftweb.http.{RequestVar, S, SHtml}
import net.liftweb.http.SHtml.ajaxSubmit
import net.liftweb.util.{CssSel, PassThru}
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.tables.Space
import net.scalapro.liftportal.util.{DB, Tags}

import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import net.liftweb.http.js.JsCmds._

import scala.xml.{NodeSeq, Text}

/**
  * Created by kreml on 08.03.2017.
  */

object t extends RequestVar("")
object AddSpace  {

  private var id: String = "0"

  def render: CssSel = {
    id = S.param("t").openOr("0")
    if(id == "0") return "*" #> ""

    t.set(id)

      "#spaces *" #> net.liftweb.http.Templates("cms"::"_space-table"::Nil) &
      ".title *" #> "Add a Space" &
        "type=submit" #> SHtml.ajaxButton(<span><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add new space</span>, process _)

  }

  private def process(): JsCmd = {

    val db = DB.getDatabase
    try {

      val q = Space.table += Space(None, None, Some(id.toInt), None)

      //      println(q.statements  )
      Await.result(
        db.run(q)
        , Duration.Inf)
    }
    finally {
      db.close
//      S.redirectTo("templates")
    }
    t.set(id)
    net.liftweb.http.Templates("cms"::"_space-table"::Nil).map(ns => SetHtml("spaces", ns)) openOr Noop
  }

}
