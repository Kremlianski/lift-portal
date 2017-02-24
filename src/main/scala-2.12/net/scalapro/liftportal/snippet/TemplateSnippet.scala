package net.scalapro.liftportal.snippet

import scala.xml.{NodeSeq, Unparsed}
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.model.cms.PageTemplatesTable
import net.scalapro.liftportal.util.DB
import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
//import net.scalapro.liftportal.util.FutureBinds._
import scala.util.{Failure, Success}

import scala.xml.{Elem, Node, XML}

/**
  * Created by EXXO on 05.02.2017.
  */
object TemplateSnippet {

  //    private  val defaultTemplate =
  //      """
  //        |<html>
  //        |<head>
  //        |    <meta charset="UTF-8" />
  //        |    <title data-lift="Menu.title">App: </title>
  //        |
  //        |</head>
  //        |<body>
  //        |<div id="exxo-template">
  //        |    Custom GENERATED content!
  //        |    <div id="exxo-content"></div>
  //        |</div>
  //        |
  //        |</body>
  //        |</html>
  //      """.stripMargin

  def render = {
    val db = DB.getDatabase
    try {
      val q = PageTemplatesTable.filter(_.id === 1.toLong).map(_.markup) //The Query


      Await.result(
        db.run(q.result) // Future[Seq[String]]
          .map(i => XML.loadString(i.head)) // parse XML
          .map {

            "html" #> _ andThen
              "#exxo-content *" #> "The end"

          }, Duration(2, "second")
      )

    }
    finally {
      db.close
    }
  }
}
