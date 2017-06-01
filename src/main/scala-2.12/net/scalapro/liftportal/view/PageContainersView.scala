package net.scalapro.liftportal.view

import net.liftweb.common.{Box, Empty}
import net.liftweb.http.{S, SHtml}
import net.liftweb.http.js.JE.{JsRaw, JsVar}
import net.liftweb.http.js.JsCmds.{Function, Noop, Replace, Script}
import net.liftweb.json.DefaultFormats
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.tables.Space
import net.scalapro.liftportal.cms.views.TempContainerV
import net.scalapro.liftportal.util.DB
import net.scalapro.liftportal.cms.views._
import net.scalapro.liftportal.util.Vars.pageId

import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.{NodeSeq, XML}


object PageContainersView {

  case class ContainerTemplate(

                             ctype: String,
                             cid: String,
                             isNew: Boolean = false
                           )

  case class SpaceContainer(
                            id: String,
                            container: Option[String],
                            content: Seq[ContainerTemplate],
                            level: Int
                          )

  implicit val formats = DefaultFormats

  def edit(): NodeSeq = {
    val id: String = S.param("id").getOrElse(pageId.is)

    pageId(id)
    val db = DB.getDatabase
    try {
      val q = Space.table.filter(s => s.main === true).filter(s => s.page_id === id.toInt).map(_.id)


      val spaceId: Int = Await.result(
        db.run(q.result)
          .map(_.head)
        , Duration.Inf
      )


      val result = <html>
        <head>
          <link href="/classpath/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet"></link>
          <script src="/classpath/lib/jquery.min.js" type="text/javascript"></script>
        </head>
        <body>
          <div class="space space-top" lift={"SpaceSnippet?id=" + spaceId}></div>
        </body>
      </html>

      transform(result)


    }
    finally {
      db.close
    }
  }

  def containers(): Seq[ContainerV] = {
    val db = DB.getDatabase
    try {
      val q = ContainerV.view //The Query


      val result = Await.result(
        db.run(q.result) // Future[Seq[ContainerV]]
          .map(i => i)
        , Duration(2, "second")
      )
      result

    }
    finally {
      db.close
    }
  }



  def tempContainer(id: Int): TempContainerV = {
    val db = DB.getDatabase
    try {
      val q = TempContainerV.view.filter(_.id === id) //The Query


      val result = Await.result(
        db.run(q.result) // Future[Seq[TempContainerV]]
          .map(i => i.head)
        , Duration(2, "second")
      )
      result

    }
    finally {
      db.close
    }
  }


  private def selectContainer(): NodeSeq = {
    val s: Seq[ContainerV] = containers()
    val ns = <div class="form-group">
      <select id="containers"  class="form-control">
        <option value="0">-------------</option>{s.map(i =>
        <option value={i.id.getOrElse(0).toString}>
          {i.name}
        </option>)}
      </select>
      <button class="btn btn-primary" id="calculate">calculate</button>
    </div>
    ns
  }


  private def transform = {
    //Add the Editor Panel
    "body -*" #>
      <div class="container-fluid">
        <div class="row" id="top-panel">
          <div id="editor-panel" class="col-md-6">
            <div id="widget" data-xx-w="0">Snippet</div>
          </div>
          <div id="controlles-panel" class="col-md-6">
          </div>
        </div>
      </div> andThen
      "#controlles-panel *+" #> {
        selectContainer
      } andThen
      "body -*" #> <script data-lift="head" type="text/javascript" src="/classpath/js/page-containers.js"></script> &
        "body -*" #> <script data-lift="head" type="text/javascript" src="/classpath/lib/Sortable.min.js"></script> &
        "body -*" #> <lift:head>
          <link href="/classpath/css/template.css" rel="stylesheet"></link>
          {Script(

            Function("loadHtml", List("id"),
              SHtml.ajaxCall(
                JsVar("id"),
                id => {

                  val containerV = tempContainer(id.toInt)


                  val transformAjax = {
                    "data-xx-role=c [data-xx-cid]" #> {
                      containerV.tempId
                    } andThen
                      "data-xx-role=c [data-xx-container]" #> {id} andThen
                      "data-xx-role=c [data-xx-role]" #> (Empty: Box[String])
                  } andThen
                    ".space [data-xx-c]" #> {containerV.tempId}


                  val container = transformAjax(XML.loadString(containerV.markup))


                  Replace("target", container) &
                    JsRaw(
                      "createSpaces();"

                    ).cmd
                }
              ).cmd
            )& Function("save", List("containers"),
              SHtml.jsonCall(JsVar("containers"), spacesJson => {

                val spaces = spacesJson.extract[List[SpaceContainer]]


                val containers = spaces.flatMap(sc => sc.content
                  .zipWithIndex
                  .map(cc => (cc._1, sc.container, sc.id, sc.level, cc._2)))
                  .groupBy(_._1)
                  .map(x => x._2.maxBy(_._4)).toSeq
                  .sortBy(_._4)
                  .map(co =>
                    PContainerV(co._1.cid, co._1.ctype.toInt, pageId.is.toInt, co._2, co._3.toInt, co._5, None)
                  )


                updateDB(containers)

                Noop
              }).cmd
            )
          )}
        </lift:head>
  }
  private def updateDB(containers: Seq[PContainerV]): Unit = {

    val db = DB.getDatabase

    val action = db.run(DBIO.seq(

      PContainerV.view ++= containers

    ))
    try Await.result(action, Duration.Inf)

    finally db.close


  }
}
