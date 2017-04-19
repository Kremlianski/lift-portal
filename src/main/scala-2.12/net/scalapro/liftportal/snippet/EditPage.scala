package net.scalapro.liftportal.snippet

import net.liftweb.http.{S, SHtml, StatefulSnippet}
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.views.PageV
import net.scalapro.liftportal.util.DB

import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global


class EditPage extends StatefulSnippet {
  private var id: String = "0"
  private var templateId: String = "0"
  private var title = ""
  private var cssClass = ""
  private var description = ""
  private var keywords = ""

  def dispatch = {
    case "render" => render
  }

  private def insert: Boolean = {

    title = ""
    description = ""
    keywords = ""
    cssClass = ""


    true
  }

  private def update(i: String): Boolean = {

    val db = DB.getDatabase
    try {
      val q = PageV.view.filter(_.id === i.toInt) //The Query


      Await.result(
        db.run(q.result.head).map { pt => //PageTemplate

          title = pt.title
          description =  pt.description.getOrElse("")
          keywords =  pt.keywords.getOrElse("")
          cssClass =  pt.pageClass.getOrElse("")

        }, Duration(2, "second")
      )

    }
    finally {
      db.close
    }


    false
  }

  def render = {
    id = S.param("t").openOr("0")
    val newbie = id match {
      case "0" => insert
      case x => update(x)
    }

    (newbie match {
      case true =>".title *" #> "Insert new Template"
      case false =>".title *" #>  s"Edit the ${title} template"
    }) &
      "name=name" #> SHtml.text(title, title = _) &
      "name=text" #> SHtml.textarea(keywords, keywords = _) &
      "name=descr" #> SHtml.text(description, description = _) &
      "name=css-class" #> SHtml.text(cssClass, cssClass = _) &
      "type=submit" #> SHtml.onSubmitUnit(process)


  }

  private def process(){
    if(title == "") {
      S.error("error", <div class="alert alert-danger" role="alert">You must define the name!</div>)
      return
    }

    val db = DB.getDatabase
    try {

      val q = PageV.view.insertOrUpdate(PageV(
        id match {
          case "0" => None
          case x: String => Some(x.toInt)
        },
        templateId.toInt,
        title,
        Some(description),
        Some(keywords),
        "general" ,
        Some(cssClass)
      ))

      Await.result(
        db.run(q)
        , Duration.Inf)
    }
    finally {
      db.close
      S.redirectTo("templates")
    }
  }
}
