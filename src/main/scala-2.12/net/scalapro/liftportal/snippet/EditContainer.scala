package net.scalapro.liftportal.snippet

import net.liftweb.common.{Empty, Failure, Full}
import net.liftweb.http.{S, SHtml, StatefulSnippet}
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.views.ContainerV
import net.scalapro.liftportal.util.DB

import scala.xml.Text
import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global


class EditContainer extends StatefulSnippet {
  private var id: String = "0"
  private var name = ""
  private var markup = ""
  private var description = ""
  private var kind = "cms"

  def dispatch = {
    case "render" => render
  }

  def render = {

    var title = "New Container"

    S.param("c") match {
      case Full(i) =>
        id = i
        val db = DB.getDatabase
        try {
          val q = ContainerV.view.filter(_.id === i.toInt) //The Query


          Await.result(
            db.run(q.result.head).map {
              cont => //PageTemplate
                name =  cont.name

                description = cont.description.getOrElse("")

                markup = cont.markup

                kind = cont.kind

                title = s"Edit the ${name} container"


            }, Duration(2, "second")
          )

        }
        finally {
          db.close
        }
      case _ =>

    }

    ".title *" #> Text(title) &
      "name=name" #> SHtml.text(name, name = _) &
      "name=text" #> SHtml.textarea(markup, markup = _) &
      "name=descr" #> SHtml.text(description, description = _) &
      "type=submit" #> SHtml.onSubmitUnit(process)
  }

  private def process() {
    if (name == "") {
      S.error("error", <div class="alert alert-danger" role="alert">You must define the name!</div>)
      return
    }

    if (markup == "") {
      S.error("error", <div class="alert alert-danger" role="alert">You must define the markup!</div>)
      return
    }

    val db = DB.getDatabase
    try {

      val q = ContainerV.view.insertOrUpdate(ContainerV(
        id match {
          case "0" => None
          case x: String => Some(x.toInt)
        },
        name,
        Some(description),
        markup,
        kind
      ))

      //      println(q.statements  )
      Await.result(
        db.run(q)
        , Duration.Inf)
    }
    finally {
      db.close
      S.redirectTo("containers")
    }
  }
}
