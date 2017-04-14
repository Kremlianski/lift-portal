package net.scalapro.liftportal.setup

import net.scalapro.liftportal.util.DB

import scala.concurrent.{Await}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import net.scalapro.liftportal.cms.tables._
import net.scalapro.liftportal.cms.SQLFunctions
import net.scalapro.liftportal.cms.Sequences
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
      |    <link rel="stylesheet" href="/classpath/lib/bootstrap/css/bootstrap.min.css" />
      |    <link rel="stylesheet" href="/classpath/css/template.css" />
      |    <script type="text/javascript" src="/classpath/lib/jquery.min.js"></script>
      |</head>
      |<body>
      |<div class="container-fluid">
      |    <div class="row">
      |        <div class="col-md-6"></div>
      |        <div class="col-md-6"></div>
      |    </div>
      |    <div id="xx-page"></div>
      |</div>
      |</body>
      |</html>
    """.stripMargin



    val cmsSchema =

      Container.table.schema ++
        Page.table.schema ++
        PContainer.table.schema ++
        Space.table.schema ++
        PWidget.table.schema ++
        Template.table.schema ++
        TWidget.table.schema




    val createSeq = DBIO.seq(

      Sequences.create,

      cmsSchema.create,

      SQLFunctions.create,

      Views.create,



      Template.table += Template(None, "default", None, defaultTemplate)

    )

    val dropSeq =  DBIO.seq(

      Views.drop,

      SQLFunctions.drop,

      Sequences.drop,

      cmsSchema.drop
    )


    def createAll = db.run(createSeq)

    def dropAll = db.run(dropSeq)

    def recreateAll = db.run(dropSeq >> createSeq)

    try Await.result(dropAll, Duration.Inf)

    finally db.close
  }

}