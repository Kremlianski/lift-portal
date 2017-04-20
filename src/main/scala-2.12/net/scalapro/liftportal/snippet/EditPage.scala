package net.scalapro.liftportal.snippet

import net.liftweb.common.{Empty, Full}
import net.liftweb.http.{S, SHtml, StatefulSnippet}
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.views.{PageV, TemplateV}
import net.scalapro.liftportal.util.DB

import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global


class EditPage extends StatefulSnippet {
  private var id: String = "0"
  private var templateId: String = "1"
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
      val q = PageV.view.filter(_.id === i.toInt)


      Await.result(
        db.run(q.result.head).map { pt =>

          title = pt.title
          templateId = pt.template_id.toString
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

  private def getTemplates = {
    val db = DB.getDatabase
    try {
      val q = TemplateV.view.map(o => o)
      val action = q.result
      val templates = Await.result(db.run(action), Duration.Inf)
      templates.map(x => (x.id.getOrElse(1).toString, x.name))
    }
    finally db.close
  }

  def render = {
    id = S.param("p").openOr("0")
    val newbie = id match {
      case "0" => insert
      case x => update(x)
    }

    (newbie match {
      case true =>".title *" #> "Insert new Page"
      case false =>".title *" #>  s"Edit the ${title} page"
    }) &
      "name=name" #> SHtml.text(title, title = _) &
      "#templates" #> SHtml.select(getTemplates, Full(templateId), templateId = _, "class"->"form-control") &
      "name=keywords" #> SHtml.text(keywords, keywords = _) &
      "name=descr" #> SHtml.text(description, description = _) &
      "name=css-class" #> SHtml.text(cssClass, cssClass = _) &
      "type=submit" #> SHtml.onSubmitUnit(process)


  }

  private def process(){
    if(title == "") {
      S.error("error", <div class="alert alert-danger" role="alert">You must define the title!</div>)
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
      S.redirectTo("pages")
    }
  }
}
