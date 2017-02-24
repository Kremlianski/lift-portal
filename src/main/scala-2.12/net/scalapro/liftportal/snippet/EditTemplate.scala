package net.scalapro.liftportal.snippet

import net.liftweb.http.{S, SHtml, StatefulSnippet}
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.model.cms.{PageTemplate, PageTemplatesTable}
import net.scalapro.liftportal.util.DB

import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global


class EditTemplate extends StatefulSnippet {
  private var id: String = "0"
  private var name = ""
  private var markup = ""
  private var description = ""

  def dispatch = {
    case "render" => render
  }

  def render = {
    id = S.param("t").openOr("0")
    val i = id match {
      case "0" => "1"
      case x => x
    }


    val db = DB.getDatabase
    try {
      val q = PageTemplatesTable.filter(_.id === i.toLong) //The Query


      Await.result(
        db.run(q.result.head).map { pt => //PageTemplate
          name = id match {
            case "0" => ""
            case _ => pt.name
          }
          description = id match {
            case "0" => ""
            case _ => pt.description.getOrElse("")
          }
          markup = pt.markup

          (id match {
            case "0" =>".title *" #> "Insert new Template"
            case _ =>".title *" #>  s"Edit the ${name} template"
          }) &
          "name=name" #> SHtml.text(name, name = _) &
            "name=text" #> SHtml.textarea(markup, markup = _) &
            "name=descr" #> SHtml.text(description, description = _) &
            "type=submit" #> SHtml.onSubmitUnit(process)

        }, Duration(2, "second")
      )

    }
    finally {
      db.close
    }
  }

  private def process(){
    if(name == "") {
      S.error("error","You must define the name!")
      return
    }

    val db = DB.getDatabase
    try {
      Await.result(
        db.run(PageTemplatesTable.insertOrUpdate(PageTemplate(
          id match {
            case "0" => None
            case x: String => Some(x.toLong)
          },
          name,
          Some(description),
          markup
        )))
        , Duration.Inf)
    }
    finally {
      db.close
      S.redirectTo("templates")
    }
  }
}
