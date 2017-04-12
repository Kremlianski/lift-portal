package net.scalapro.liftportal.snippet

import net.liftweb.http.js.JsCmd
import net.liftweb.http.SHtml
import net.liftweb.http.js.JE.{JsRaw, Str}
import net.liftweb.util.CssSel
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.tables.Space
import net.scalapro.liftportal.util.{DB, Tags}
import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._
import net.scalapro.liftportal.util.SpaceUtil
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import net.liftweb.http.js.JsCmds._


object SpaceTable  {

  private var id = ("","0")

  def render:CssSel = {


    id = t.is

    if (id._2 == "0") {
      return "*" #> ""

    }

    val q = id match {
      case ("t", x) => Space.table.filter(_.template_id === x.toInt)
      case ("c", x) => Space.table.filter(_.container_id === x.toInt)
      case _ => return  "*" #> ""
    }

    val db = DB.getDatabase
    try {

      val l:Seq[Space] = Await.result(db.run(q.result).map(_.map(o=>o)), Duration(2, "second"))

      ".rows" #> l.map {
        x =>
          <tr>
            {Tags.td(x.id.getOrElse("").toString)}
            {Tags.td(SpaceUtil.getMarkup(x.id.getOrElse(0)))}
               <td>
              {SHtml.ajaxButton(<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>, Str(x.id.getOrElse(0).toString), drop _, "class"->"btn btn-default btn-sm")}
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
