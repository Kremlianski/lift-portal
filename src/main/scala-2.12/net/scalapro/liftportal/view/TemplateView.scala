package net.scalapro.liftportal.view

import net.liftweb.common.{Box, Empty, Full}
import net.liftweb.http.{JsContext, JsonContext, RequestVar, S, SHtml}
import net.liftweb.http.js.JE.{Call, JsRaw, JsVal, JsVar}
import net.liftweb.http.js.JsCmds.{Alert, Function, Noop, Replace, Script, SetHtml}
import net.liftweb.http.js.jquery.JqJsCmds.JqSetHtml
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonDSL._
import net.liftweb.json._
import net.liftweb.util.Helpers._
import net.liftweb.util.PassThru
import net.scalapro.liftportal.cms.views.{TempContainerV, TemplateV}
import net.scalapro.liftportal.util.DB
import net.scalapro.liftportal.cms.views._

import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.collection.immutable.WrappedString
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.{NodeSeq, Text, XML}
import net.scalapro.liftportal.cms.tables.{TWidget, Widget, Widgets}
import net.scalapro.liftportal.cms.views.TemplateWidgetsV.TemplatesWidgetsV
import net.liftweb.util.CssSel

/**
  * Created by kreml on 21.03.2017.
  */


//stores the spaces with widgets







object TemplateView {

  type WidgetsMap = Map[Int,Seq[TWidgetV]]
  object spacesStorage extends RequestVar[WidgetsMap](Map.empty)

  case class WidgetTemplate (

                              wtype: String,
                              wid: String,
                              isNew: Boolean = false
                            )
  case class SpaceTemplate (
                             id: String,
                             content: Seq[WidgetTemplate]
                           )




  implicit val formats = DefaultFormats



  private  def getTemplate(id: String): Seq[TemplateWidgetsV] = {
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
    val id = S.param("id").getOrElse("1")

      val template = getTemplate(id)

      val markup = XML.loadString(template.head.markup)

      val widgets = template.map(_.extractWidget)

//
//      def makeTemplate = {
//
//
//        val spaces = widgets.groupBy(_.space_id)
//        val iterator = spaces.keys.iterator
//
//        def insertSpace = {
//          if(iterator.hasNext) {
//            val space = iterator.next
//            (("lift=SpaceSnippet?id="+space+ " *") #> Text(space.toString+"Fuck"))
//          } else {
//            PassThru
//          }
//
//        }
//
//        insertSpace
//
////        val body = "body "
////        (body + "[class]") #> "super-class"
//      }




          val spaces = widgets.groupBy(_.space_id)

          spacesStorage(spaces)


//      val q = TemplateV.view.filter(_.id === 1).map(_.markup) //The Query
//
//
//      val result = Await.result(
//        db.run(q.result) // Future[Seq[String]]
//          .map(i => XML.loadString(i.head)) // parse XML
//        , Duration(2, "second")
//      )

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
      <select id="containers"  class="form-control">
        <option value="0">-------------</option>{s.map(i =>
        <option value={i.id.toString}>
          {i.name}
        </option>)}
      </select>
      <button class="btn btn-primary" id="calculate">calculate</button>
    </div>
    ns
  }

  private def updateDB(spaces: List[SpaceTemplate]): Unit = {
    val result = spaces.map(x=>{
      val spaceId = x.id
      x.content.zipWithIndex.map(y=>{
        val widget = y._1
        val order = y._2
        TWidgetV(widget.wid, widget.wtype.toInt, 1, spaceId.toInt, order, None, None)
      })
    }).flatten

    val db = DB.getDatabase
    val action = db.run(DBIO.seq(

      TWidgetV.view ++= result

    ))
    try Await.result(action, Duration.Inf)

    finally db.close
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
                  val classSnippet = "lift:WidgetSnippet?"+id
                  val container = <div class={classSnippet}></div>
                  val delim = id.indexOf(';')


                  var i:Int = 0

                  if(delim >=0) i = id.substring(3, delim).toInt
                  else i = id.substring(3).toInt

                  val func:String = Widgets.get(i).jsFunc

                  if(func != ""){
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
