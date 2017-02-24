package net.scalapro.liftportal.snippet

import net.liftweb.common.Full
import net.liftweb.http.S

import scala.xml.{NodeSeq, Unparsed}
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.model.cms.{PageTemplate, PageTemplatesTable}
import net.scalapro.liftportal.util.DB

import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
//import net.scalapro.liftportal.util.FutureBinds._
import scala.util.{Failure, Success}

import scala.xml.{Elem, Node, XML}


object Templates {

  private def removeTemplate(id:String): Unit = {
    if(id == "1") {
      S.error("error","You can't remove the default template!")
      return
    }
    val q = PageTemplatesTable.filter(_.id === id.toLong)
    val action = q.delete
    val db = DB.getDatabase
    try {
      Await.result(
        db.run(action)
        , Duration.Inf)
    }
    finally {
      db.close
      S.redirectTo("templates")
    }
  }
  def render = {

    S.param("d") match {
      case Full(x) => removeTemplate(x)
      case _ =>
    }
    val db = DB.getDatabase
    try {
      val q = PageTemplatesTable.map(o=>o)
      val action= q.result
//      println(action.statements.head)

      Await.result(
        db.run(action) // Future[Seq[String]]
          .map { p =>

          ".rows" #> p.map(x => {
             <tr>
              <td>
                <a href={"edit-template?t="+x.id.getOrElse("")+""}>
                  {x.name}
                </a>
              </td>
              <td>
              {x.description.getOrElse("")}
              </td>
               <td>
                 <a href={"?d="+x.id.getOrElse("")+""}>
                   Remove
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
