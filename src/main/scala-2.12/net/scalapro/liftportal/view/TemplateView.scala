package net.scalapro.liftportal.view

import net.liftweb.http.SHtml
import net.liftweb.http.js.JE.{Call, JsVal, JsVar}
import net.liftweb.http.js.JsCmds.{Alert, Function, Script}
import net.liftweb.http.js.jquery.JqJsCmds.JqSetHtml
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
      "body -*" #> <script data-lift="head" type="text/javascript" src="/classpath/js/bundle.js"></script> andThen
        "body -*" #> <script data-lift="head" type="text/javascript" src="/classpath/lib/Sortable.min.js"></script> andThen
        "body -*" #> <div id="editor-panel"><div id="widget">Snippet</div></div> andThen
        "body -*" #> <lift:head>{
          Script(
            Function("sortableUpdateServerCallback", List("paramName"),
              SHtml.ajaxCall(
                JsVar("paramName"),
                (paramName: String) => {JqSetHtml("editor-panel", <div></div>)}
              )._2.cmd
            )
          )}</lift:head>
      }
}
