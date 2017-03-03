package net.scalapro.liftportal.setup

import net.scalapro.liftportal.util.DB

import scala.concurrent.{Await}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import net.scalapro.liftportal.cms.tables._
import net.scalapro.liftportal.cms.SQLFunctions
import net.scalapro.liftportal.cms.views._


object DBSetup {

  private val setup_? = true


  def setup: Unit = {

    if (!setup_?) return


    val db = DB.getDatabase

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

      DBIO.seq(
        cmsSchema.create,

        SQLFunctions.create,

        Views.create,

        Template.table += Template(None, "default", None, defaultTemplate)

      )
    )

    def dropAll = db.run(

      DBIO.seq(

        Views.drop,

        SQLFunctions.drop,

        cmsSchema.drop
      )

    )



    try {
      Await.result(createAll, Duration.Inf)

    } finally db.close
  }

}