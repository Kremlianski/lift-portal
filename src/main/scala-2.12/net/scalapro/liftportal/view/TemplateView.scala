package net.scalapro.liftportal.view

import net.liftweb.common.{Empty, Full}
import net.liftweb.http.{JsContext, JsonContext, SHtml}
import net.liftweb.http.js.JE.{Call, JsRaw, JsVal, JsVar}
import net.liftweb.http.js.JsCmds.{Alert, Function, Noop, Replace, Script, SetHtml}
import net.liftweb.http.js.jquery.JqJsCmds.JqSetHtml
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonDSL._
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.views.TemplateV
import net.scalapro.liftportal.util.DB

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

  private def transform = {
    //Add the Editor Panel
    "body -*" #> <div id="editor-panel">
      <div id="widget">Snippet</div>
    </div> andThen
      "body -*" #> <script data-lift="head" type="text/javascript" src="/classpath/js/bundle.js"></script> &
        "body -*" #> <script data-lift="head" type="text/javascript" src="/classpath/lib/Sortable.min.js"></script> &
        "body -*" #> <lift:head>
          {Script(

            Function("loadHtml", List("id"),
              SHtml.ajaxCall(
                JsVar("id"),
                id => {
                  val s:String =
                    """
                      |
                      |<div class="container-fluid">
                      |    <div class="row">
                      |        <div class="col-md-6"><div class="lift:SpaceSnippet?id=3"></div></div>
                      |        <div class="col-md-6"><div class="lift:SpaceSnippet?id=4"></div></div>
                      |    </div>
                      |</div>
                      |
                    """.stripMargin

                  val container =  XML.loadString(s)


                  Replace("target", container)&
                  JsRaw(
                      "createSpaces();"

                  ).cmd
                }
              ).cmd
            )
          )
          }
        </lift:head>
  }
}
