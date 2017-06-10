package net.scalapro.liftportal.view

import net.liftweb.common.{Box, Empty}
import net.liftweb.http.{S, SHtml}
import net.liftweb.http.js.JE.{JsRaw, JsVar}
import net.liftweb.http.js.JsCmds.{Function, Noop, Replace, Script}
import net.liftweb.json.DefaultFormats
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.tables.{Space, Widget, Widgets}
import net.scalapro.liftportal.cms.views.TempContainerV
import net.scalapro.liftportal.util.DB
import net.scalapro.liftportal.cms.views._
import net.scalapro.liftportal.util.Vars.{containersStorage, isContainerEdit, markupStorage, pageId}

import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.{NodeSeq, XML}



object PageEditView {

  case class ContainerTemplate(
                                cid: Option[String],
                                ctype: Option[String],
                                wid: Option[String],
                                wtype: Option[String],
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

    setStorage(id)

    isContainerEdit(true)


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
          <div class="space space-top" lift={"SpaceContainerSnippet?id=" + spaceId}></div>
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
    val w: Seq[Widget] = Widgets.get
    val s: Seq[ContainerV] = containers()
    val ns = <div class="form-group">
      <select id="containers"  class="form-control">
        <option value="0">-------------</option>{s.map(i =>
        <option value={i.id.getOrElse(0).toString}>
          {i.name}
        </option>)}
      </select>
      <select id="widgets"  class="form-control">
        <option value="0">-------------</option>{w.map(i =>
        <option value={i.id.toString}>
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
            <div id="container" data-xx-con="0">Container</div>
            <div id="widget" data-xx-w="0">Widget</div>
          </div>
          <div id="controlles-panel" class="col-md-6">
          </div>
        </div>
      </div> andThen
      "#controlles-panel *+" #> {
        selectContainer
      } andThen
      "body -*" #> <script data-lift="head" type="text/javascript" src="/classpath/js/page-edit.js"></script> &
        "body -*" #> <script data-lift="head" type="text/javascript" src="/classpath/lib/Sortable.min.js"></script> &
        "body -*" #> <lift:head>
          <link href="/classpath/css/template.css" rel="stylesheet"></link>
          {Script(

            Function("loadWidget", List("id"),
              SHtml.ajaxCall(
                JsVar("id"),
                id => {
                  val classSnippet = "lift:WidgetSnippet?" + id
                  val container = <div class={classSnippet}></div>
                  val delim = id.indexOf(';')


                  var i: Int = 0

                  if (delim >= 0) i = id.substring(3, delim).toInt
                  else i = id.substring(3).toInt

                  val func: String = Widgets.get(i).jsFunc

                  if (func != "") {
                    Replace("target", container) &
                      JsRaw(
                        s"${func}(${i})"
                      ).cmd
                  }
                  else {
                    Replace("target", container)
                  }
                }
              ).cmd
            ) & Function("loadContainer", List("id"),
              SHtml.ajaxCall(
                JsVar("id"),
                id => {

                  val containerV = tempContainer(id.toInt)


                  val transformAjax = {
                    "data-xx-role=c [data-xx-cid]" #> {
                      containerV.tempId
                    } andThen
                      "data-xx-role=c [data-xx-container]" #> id andThen
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
            ) & Function("save", List("containers"),
              SHtml.jsonCall(JsVar("containers"), spacesJson => {



                val spaces = spacesJson.extract[List[SpaceContainer]]


                val containers = spaces.flatMap(sc => sc.content
                  .zipWithIndex
                  .map(cc => (cc._1, sc.container, sc.id, sc.level, cc._2)))
                  .groupBy(_._1)
                  .map(x => x._2.maxBy(_._4)).toSeq
                  .sortBy(_._4)
                  .map {
                    case (ContainerTemplate(Some(cid), Some(ctype), None, None, _), c2, c3, _, c5) =>
                      PContainerV(cid, ctype.toInt, pageId.is.toInt, c2, c3.toInt, c5, None)
                    case (ContainerTemplate(None, None, Some(wid), Some(wtype), _), c2, c3, _, c5) =>
                      PWidgetV(wid, wtype.toInt, pageId.is.toInt, c3.toInt, c2, None, c5, None)
                  }


                updateDB(containers)

                Noop
              }).cmd
            )
          )}
        </lift:head>
  }
  private def updateDB(containers: Seq[PItemV]): Unit = {

    val db = DB.getDatabase

    var pContainers = List.empty[PContainerV]
    var pWidgets = List.empty[PWidgetV]

    containers.foreach {
      case x: PContainerV => pContainers :+= x
      case x: PWidgetV => pWidgets :+= x
    }



    val action = db.run(DBIO.seq(

      PContainerV.view ++= pContainers,

      PWidgetV.view ++= pWidgets

    ))

    try Await.result(action, Duration.Inf)

    finally db.close


  }

  private def setStorage(id: String) {

    /*
    a view must be created for this query:


    select co.id as c_id, container_id, co.page_id as c_page_id,
co.p_container_id as c_p_container_id, co.space_id as c_space_id,
co.ord as c_ord, co.container_class as c_container_class,
w.id as w_id, widget_id, w.page_id as w_page_id, w.space_id as w_space_id,
w.p_container_id as w_p_container_id, params, w.ord as w_ord, w.container_class as w_container_class
from p_containers_v as co full join p_widgets_v as w on co.id = w.id



     */





    val db = DB.getDatabase
    val q = PContainerV.view.filter(_.page_id === id.toInt)
    val q1 = q.map(_.container_id)
    val q2 = ContainerV.view.filter(_.id in q1).distinct

    val q3 = for {
      (c, w) <- PContainerV.view joinFull PWidgetV.view on (_.id === _.id)
    } yield (c, w)

    try {

      val cont = Await.result(db.run(q.result), Duration.Inf)

      val items = Await.result(db.run(q3.result), Duration.Inf)
      println(items)

      val pContainers = cont.isEmpty match {
        case false => cont.groupBy(x => (x.space_id, x.p_container_id))
        case true => Map.empty[(Int, Option[String]), Seq[PContainerV]]
      }

      val markupsResult = Await.result(db.run(q2.result), Duration.Inf)

      val markups = markupsResult.isEmpty match {
        case false => markupsResult.map(x => (x.id.getOrElse(0), x.markup)).toMap
        case true => Map.empty[Int, String]
      }

      containersStorage(pContainers)
      markupStorage(markups)


    }

    finally db.close

  }
}
