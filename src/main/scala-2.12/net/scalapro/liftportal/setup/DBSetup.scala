package net.scalapro.liftportal.setup

import net.scalapro.liftportal.util.DB

import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
//import net.scalapro.liftportal.model.cms._
import net.scalapro.liftportal.cms.tables._
import net.scalapro.liftportal.cms.views._
import net.scalapro.liftportal.cms.SQLFunctions
import net.scalapro.liftportal.cms.views._
import scala.concurrent.ExecutionContext.Implicits.global

  /*

  Using for creating all tables and other DB operations w
  during boot process


   */


object DBSetup {

  /*

  create DB tables in Boot.scala

   */


  def setup: Unit = {
    val db =DB.getDatabase
    val cmsSchema = Container.table.schema ++ Page.table.schema ++ PContainer.table.schema ++
    Space.table.schema ++ PWidget.table.schema ++ TContainer.table.schema ++ Template.table.schema ++
    TWidget.table.schema ++ Widget.table.schema
    val defaultTemplate =
      """
        |<html>
        |<head>
        |    <meta charset="UTF-8" />
        |    <title data-lift="Menu.title">App: </title>
        |
        |</head>
        |<body>
        |<div id="exxo-template">
        |    Custom GENERATED content!
        |    <div id="exxo-content"></div>
        |</div>
        |
        |</body>
        |</html>
      """.stripMargin

        try {
          Await.result(db.run(DBIO.seq(
              cmsSchema.create
//              PageTemplatesTable += PageTemplate(None,"default", None, defaultTemplate)

//            cmsSchema.drop
//

          )

            andThen SQLFunctions.create

            andThen Views.create



          ), Duration.Inf)


//          val q = PageTemplatesTable.filter(_.id === 1.toLong).map(_.markup)
//          val result = db.run(q.result)
//          result.onComplete(println(_))

        } finally db.close
  }

}