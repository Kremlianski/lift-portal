package net.scalapro.liftportal.view

import net.liftweb.common.{Box, Empty, Full}
import net.liftweb.http.{JsContext, JsonContext, SHtml}
import net.liftweb.http.js.JE.{Call, JsRaw, JsVal, JsVar}
import net.liftweb.http.js.JsCmds.{Alert, Function, Noop, Replace, Script, SetHtml}
import net.liftweb.http.js.jquery.JqJsCmds.JqSetHtml
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonDSL._
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.views.{TempContainerV, TemplateV}
import net.scalapro.liftportal.util.DB
import net.scalapro.liftportal.cms.views._
import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.collection.immutable.WrappedString
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.{NodeSeq, Text, XML}

/**
  * Created by kreml on 21.03.2017.
  */
object TemplateView {
  def edit(id: String): NodeSeq = {
    val db = DB.getDatabase
    try {
      val q = TemplateV.view.filter(_.id === 1).map(_.markup) //The Query


      val result = Await.result(
        db.run(q.result) // Future[Seq[String]]
          .map(i => XML.loadString(i.head)) // parse XML
        , Duration(2, "second")
      )

      transform(result)


    }
    finally {
      db.close
    }
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
    val s: Seq[ContainerV] = containers()
    val ns = <div class="form-group">
      <select id="containers"  class="form-control">
        <option value="0">-------------</option>{s.map(i =>
        <option value={i.id.getOrElse(0).toString}>
          {i.name}
        </option>)}
      </select>
    </div>
    ns
  }


  private def transform = {
    //Add the Editor Panel
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
      "body -*" #> <script data-lift="head" type="text/javascript" src="/classpath/js/bundle.js"></script> &
        "body -*" #> <script data-lift="head" type="text/javascript" src="/classpath/lib/Sortable.min.js"></script> &
        "body -*" #> <lift:head>
          {Script(

            Function("loadHtml", List("id"),
              SHtml.ajaxCall(
                JsVar("id"),
                id => {

                  val containerV = tempContainer(id.toInt)


                  val transformAjax = {
                    "data-xx-role=c [data-xx-c]" #> {
                      containerV.tempId
                    } andThen
                      "data-xx-role=c [data-xx-role]" #> (Empty: Box[String])
                  }


                  val container = transformAjax(XML.loadString(containerV.markup))


                  Replace("target", container) &
                    JsRaw(
                      "createSpaces();"

                    ).cmd
                }
              ).cmd
            )
          )}
        </lift:head>
  }
}
