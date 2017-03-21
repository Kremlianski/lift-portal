package net.scalapro.liftportal.view

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

    "body -*" #> <div id="editor-panel"><div id="widget">Snippet</div></div>
  }
}
