package net.scalapro.liftportal.setup

import net.scalapro.liftportal.util.DB

import scala.concurrent.{Await}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import net.scalapro.liftportal.cms.tables._
import net.scalapro.liftportal.cms.SQLFunctions
import net.scalapro.liftportal.cms.views._


object DBSetup {

  private val setup_? = false


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


    val createSeq = DBIO.seq(

      cmsSchema.create,

      SQLFunctions.create,

      Views.create,

      Template.table += Template(None, "default", None, defaultTemplate)

    )

    val dropSeq =  DBIO.seq(

      Views.drop,

      SQLFunctions.drop,

      cmsSchema.drop
    )


    def createAll = db.run(createSeq)

    def dropAll = db.run(dropSeq)

    def recreateAll = db.run(dropSeq >> createSeq)



    try Await.result(recreateAll, Duration.Inf)

    finally db.close
  }

}