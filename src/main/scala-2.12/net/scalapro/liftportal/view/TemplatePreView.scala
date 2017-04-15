package net.scalapro.liftportal.view

import net.liftweb.http.{S, SHtml}
import net.liftweb.json._
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.util.DB
import net.scalapro.liftportal.cms.views._
import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.duration.Duration
import scala.xml.{NodeSeq, XML}
import net.scalapro.liftportal.util.Vars.{templateId, spacesStorage}


/**
  * Created by kreml on 21.03.2017.
  */


object TemplatePreView {

  type WidgetsMap = Map[Int, Seq[TWidgetV]]

  case class WidgetTemplate(

                             wtype: String,
                             wid: String,
                             isNew: Boolean = false
                           )

  case class SpaceTemplate(
                            id: String,
                            content: Seq[WidgetTemplate]
                          )


  implicit val formats = DefaultFormats


  private def getTemplate(id: String): Seq[TemplateWidgetsV] = {
    val db = DB.getDatabase
    val q = TemplateWidgetsV.view.filter(_.template === id.toInt)
    try {

      Await.result(
        db.run(q.result)

        , Duration(2, "second")
      )
    }
    finally db.close

  }


  def edit(): NodeSeq = {


    val id: String = S.param("id").getOrElse(templateId.is)

    templateId(id)

    val template = getTemplate(id)

    val markup = XML.loadString(template.head.markup)

    val widgets = template.map(i => i.extractWidget).filterNot(_ == None).map(_.get)


    val spaces = widgets.size == 0 match {
      case false => widgets.groupBy(_.space_id)
      case true => Map.empty[Int, Seq[TWidgetV]]
    }

    spacesStorage(spaces)
    transform(markup)


  }



  def close(): Unit ={
    val id = templateId.is
    S.redirectTo("template", ()=>templateId(id))
  }


  private def transform = {
    val id = templateId.is
    "body -*" #>
      <div class="container-fluid">
        <div class="row" id="top-panel">
          <div id="controlles-panel" class="col-md-12">
          </div>
        </div>
      </div> andThen
      "#controlles-panel *+" #> {SHtml.link("", ()=> {
        templateId(id)
        close
      }, <span>
        <span class="glyphicon glyphicon-th"></span> back</span>, "class"->"btn btn-primary" )}
  }
}

