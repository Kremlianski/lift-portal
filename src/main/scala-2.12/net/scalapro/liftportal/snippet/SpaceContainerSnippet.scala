package net.scalapro.liftportal.snippet

import net.liftweb.common.{Box, Empty}
import net.liftweb.http.S
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.tables.Widgets
import net.scalapro.liftportal.cms.views.{PContainerV, PWidgetV}
import net.scalapro.liftportal.util.Vars.{containersStorage, markupStorage}

import scala.xml.{Elem, NodeSeq, XML}

/**
  * Created by kreml on 21.03.2017.
  */
class SpaceContainerSnippet {


  def render = {
    val spaces = containersStorage.is
    val markups = markupStorage.is
    val id = S.attr("id").openOr("0")
    val containerId = S.attr("container-id").toOption

    val space = spaces.getOrElse((id.toInt, containerId), Seq.empty).sortBy(_.ord)

    "*" #> <div class="space" data-xx-sid={id}>
      {space.map{

          case i: PContainerV => {

            val markup = XML.loadString(markups.getOrElse(i.container_id, ""))

            //        val classSnippet = "lift:ContainerSnippet?id="+containerId.getOrElse("")
            var lift = ""
            val transform = {
              "data-xx-role=c [data-xx-cid]" #> {
                i.id
              } andThen
                "data-xx-role=c [data-xx-container]" #> {
                  i.container_id
                } andThen
                "data-xx-role=c [data-xx-role]" #> (Empty: Box[String])
            } andThen
              ".space [data-xx-c]" #> {
                i.id
              } andThen
              ".space" #> ((n: NodeSeq) => {
                lift = n.asInstanceOf[Elem].attribute("lift").getOrElse("").toString
                n
              }) andThen
              ".space [lift]" #> {
                lift + ";container-id=" + i.id
              }


            //        <div class="container-init">
            //          <div class={classSnippet} data-xx-cid={i.id.toString} data-xx-container={i.container_id.toString}></div>
            //        </div>
            transform(markup)
        }
          case i:PWidgetV => {
            val snippet = Widgets.get(i.widget_id).snippet
            val classSnippet = "lift:" + snippet
            <div class="widget-init">
              <div class={classSnippet} data-xx-wid={i.id} data-xx-widget={i.widget_id.toString}></div>
            </div>
          }

      }}
    </div>


  }


}
