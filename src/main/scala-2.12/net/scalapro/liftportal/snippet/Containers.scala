package net.scalapro.liftportal.snippet

import net.liftweb.common.Full
import net.liftweb.http.S

import net.liftweb.util.Helpers._

import net.scalapro.liftportal.cms.views.ContainerV

import net.scalapro.liftportal.util.DB

import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

import net.scalapro.liftportal.util.Tags
import net.scalapro.liftportal.util.Tags._


object Containers {

  private def removeContainer(id: String): Unit = {

    val q = ContainerV.view.filter(_.id === id.toInt)
    val action = q.delete
    val db = DB.getDatabase
    try {
      Await.result(
        db.run(action)
        , Duration.Inf)
    }
    finally {
      db.close
      S.redirectTo("containers")
    }
  }


  def render = {


    S.param("d") match {
      case Full(x) => removeContainer(x)
      case _ =>
    }
    val db = DB.getDatabase
    try {
      val q = ContainerV.view.map(o => o)
      val action = q.result

      Await.result(
        db.run(action) // Future[Seq[String]]
          .map { p =>

          ".rows" #> p.map(x => {
            <tr>
              {Tags.td(x.name, None, Some("edit-container?c=" + x.id.getOrElse("") + ""))}
              {Tags.td(x.description.getOrElse(""))}
              {Tags.td(x.kind)}
              <td>
              <a href={"?d=" + x.id.getOrElse("") + ""} class="btn btn-default btn-sm">
                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
              </a>
            </td>
            </tr>
          }
          )
        }, Duration(2, "second")
      )

    }
    finally {
      db.close
    }
  }
}
