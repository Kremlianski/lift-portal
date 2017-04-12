package net.scalapro.liftportal.view


import net.liftweb.http.{RequestVar, S, SHtml}
import net.liftweb.http.js.JE.{JsRaw, JsVar}
import net.liftweb.http.js.JsCmds.{Function, Noop, Replace, Script}
import net.liftweb.json._
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.views.{TempContainerV}
import net.scalapro.liftportal.util.DB
import net.scalapro.liftportal.cms.views._
import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.{NodeSeq, XML}
import net.scalapro.liftportal.cms.tables.{Widget, Widgets}


/**
  * Created by kreml on 21.03.2017.
  */


object TemplateView {

  type WidgetsMap = Map[Int, Seq[TWidgetV]]

  object spacesStorage extends RequestVar[WidgetsMap](Map.empty)
  object templateId extends RequestVar[String]("1")

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

  def containers(): Seq[ContainerV] = {
    val db = DB.getDatabase
    try {
      val q = ContainerV.view //The Query


      val result = Await.result(
        db.run(q.result) // Future[Seq[ContainerV]]
          .map(i => i)
        , Duration(2, "second")
      )
      result

    }
    finally {
      db.close
    }
  }


  def tempContainer(id: Int): TempContainerV = {
    val db = DB.getDatabase
    try {
      val q = TempContainerV.view.filter(_.id === id) //The Query


      val result = Await.result(
        db.run(q.result) // Future[Seq[TempContainerV]]
          .map(i => i.head)
        , Duration(2, "second")
      )
      result

    }
    finally {
      db.close
    }
  }


  private def selectContainer(): NodeSeq = {
    val s: Seq[Widget] = Widgets.get
    val ns = <div class="form-group">
      <select id="containers" class="form-control">
        <option value="0">-------------</option>{s.map(i =>
        <option value={i.id.toString}>
          {i.name}
        </option>)}
      </select>
      <button class="btn btn-primary" id="calculate">
        <span class="glyphicon glyphicon-save"></span> save
      </button>
      {SHtml.link("", clearAll _, <span><span class="glyphicon glyphicon-remove"></span> clear</span>, "class"->"btn btn-danger" )}
    </div>
    ns
  }

  private def updateDB(spaces: List[SpaceTemplate]): Unit = {
    val result = spaces.flatMap(x => {
      val spaceId = x.id
      x.content.zipWithIndex.map(y => {
        val widget = y._1
        val order = y._2
        TWidgetV(widget.wid, widget.wtype.toInt, 1, spaceId.toInt, order, None, None)
      })
    })

    val db = DB.getDatabase
    val action = db.run(DBIO.seq(

      TWidgetV.view ++= result

    ))
    try Await.result(action, Duration.Inf)

    finally db.close
  }
  def clearAll(): Unit ={

    val db = DB.getDatabase

    val template = templateId.is
    val q = TWidgetV.view.filter(_.template_id === template.toInt).delete


    try Await.result(db.run(q), Duration.Inf)

    finally db.close


    S.redirectTo("template")
  }
  private def transform = {
    "body -*" #>
      <div class="container-fluid">
        <div class="row" id="top-panel">
          <div id="editor-panel" class="col-md-6">
            <div id="widget" data-xx-w="0">Snippet</div>
          </div>
          <div id="controlles-panel" class="col-md-6">
          </div>
        </div>
      </div> andThen
      "#controlles-panel *+" #> {
        selectContainer

      } andThen
      "body -*" #> <script data-lift="head" type="text/javascript" src="/classpath/js/template.js"></script> &
        "body -*" #> <script data-lift="head" type="text/javascript" src="/classpath/lib/Sortable.min.js"></script> &
        "body -*" #> <lift:head>
          {Script(

            Function("loadHtml", List("id"),
              SHtml.ajaxCall(
                JsVar("id"),
                id => {
                  val classSnippet = "lift:WidgetSnippet?" + id
                  val container = <div class={classSnippet}></div>
                  val delim = id.indexOf(';')


                  var i: Int = 0

                  if (delim >= 0) i = id.substring(3, delim).toInt
                  else i = id.substring(3).toInt

                  val func: String = Widgets.get(i).jsFunc

                  if (func != "") {
                    Replace("target", container) &
                      JsRaw(
                        s"${func}(${i})"
                      ).cmd
                  }
                  else {
                    Replace("target", container)
                  }
                }
              ).cmd
            ) & Function("save", List("widgets"),
              SHtml.jsonCall(JsVar("widgets"), widgets => {
                val spaces = widgets.extract[List[SpaceTemplate]]

                updateDB(spaces)

                Noop
              }).cmd
            )
          )}
        </lift:head>
  }
}
