package net.scalapro.liftportal.view

import net.liftweb.common.{Empty, Full}
import net.liftweb.http.{JsContext, JsonContext, SHtml}
import net.liftweb.http.js.JE.{Call, JsRaw, JsVal, JsVar}
import net.liftweb.http.js.JsCmds.{Alert, Function, Script, Noop}
import net.liftweb.http.js.jquery.JqJsCmds.JqSetHtml
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonDSL._
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.views.TemplateV
import net.scalapro.liftportal.util.DB

import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.{NodeSeq, XML}

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

  private val transform = {


    //Add the Editor Panel


    "body -*" #> <div id="editor-panel">
      <div id="widget">Snippet</div>
    </div> andThen
      "body -*" #> <script data-lift="head" type="text/javascript" src="/classpath/js/bundle.js"></script> &
        "body -*" #> <script data-lift="head" type="text/javascript" src="/classpath/lib/Sortable.min.js"></script> &
        "body -*" #> <lift:head>
          {Script(


            Function("ajaxLoad", List("param"),
              SHtml.jsonCall(
                JsVar("param"),
                new JsonContext(
                  Full("ajaxSuccess"),
                  Full("ajaxError")
                ),
                (param: JValue) => {
                  // .............. load container
                  param
                }
              )._2.cmd
            ) & JsRaw(
              """
                |console.log("loaded");
              """.stripMargin).cmd
          )}
        </lift:head>
  }
}
