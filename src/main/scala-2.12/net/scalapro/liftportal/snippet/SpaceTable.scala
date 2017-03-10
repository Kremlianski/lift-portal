package net.scalapro.liftportal.snippet

import net.liftweb.http.js.JsCmd
import net.liftweb.http.S
import net.liftweb.http.SHtml
import net.liftweb.http.js.JE.{JsRaw, Str}
import net.liftweb.util.CssSel
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.tables.Space
import net.scalapro.liftportal.util.{DB, Tags}

import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.Text
import net.liftweb.http.js.JsCmds._
/**
  * Created by kreml on 08.03.2017.
  */
object SpaceTable  {

  private var id: String = "0"

  def render:CssSel = {

    id = S.param("t").openOr("0")
    val i = id match {
      case "0" => "1"
      case x => x
    }

    id = t.is

    if (id == "0") {
      return "*" #> ""

    }
    val db = DB.getDatabase
    try {
      val q = Space.table.filter(_.template_id === id.toInt) //The Query


      val l:Seq[Space] = Await.result(db.run(q.result).map(_.map(o=>o)), Duration(2, "second"))

      ".rows" #> l.map {
        x =>
          <tr>
            {Tags.td(x.id.getOrElse("").toString)}
            {Tags.td(s"""<div class="lift:SpaceSnippet?id=${x.id.getOrElse("").toString}"></div>""")}
            <td>
              {SHtml.ajaxButton(<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>, Str(x.id.getOrElse("").toString), drop _, "class"->"btn btn-default")}
            </td>

          </tr>

      }
    }
    finally {
      db.close
    }
  }

  def drop(s:String):JsCmd = {
    val db = DB.getDatabase
    try {

      val q = Space.table.filter(_.id === s.toInt)


      Await.result(
        db.run(q.delete)
        , Duration.Inf)
    }
    finally {
      db.close
    }
    t.set(id)
    net.liftweb.http.Templates("cms"::"_space-table"::Nil).map(ns => SetHtml("spaces", ns)) openOr Noop
  }
}
