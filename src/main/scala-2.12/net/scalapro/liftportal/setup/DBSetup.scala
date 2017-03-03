package net.scalapro.liftportal.setup

import net.scalapro.liftportal.util.DB

import scala.concurrent.{Await}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import net.scalapro.liftportal.cms.tables._
import net.scalapro.liftportal.cms.SQLFunctions
import net.scalapro.liftportal.cms.views._


object DBSetup {

  def setup: Unit = {

    val db = DB.getDatabase

    val cmsSchema =

      Container.table.schema ++
      Page.table.schema ++
      PContainer.table.schema ++
      Space.table.schema ++
      PWidget.table.schema ++
      TContainer.table.schema ++
      Template.table.schema ++
      TWidget.table.schema ++
      Widget.table.schema


    def createAll = db.run(

      cmsSchema.create

        andThen SQLFunctions.create

        andThen Views.create


    )

    def dropAll = db.run(

      DBIO.seq(

        Views.drop,

        SQLFunctions.drop,

        cmsSchema.drop
      )

    )


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
      Await.result(dropAll, Duration.Inf)


      //          val q = PageTemplatesTable.filter(_.id === 1.toLong).map(_.markup)
      //          val result = db.run(q.result)
      //          result.onComplete(println(_))

    } finally db.close
  }

}