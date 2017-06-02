package net.scalapro.liftportal.snippet

import net.liftweb.http.S
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.tables.Widgets
import net.scalapro.liftportal.util.Vars.containersStorage

/**
  * Created by kreml on 21.03.2017.
  */
class SpaceContainerSnippet {


  def render = {
    val spaces = containersStorage.is
    val id = S.attr("id").openOr("0")
    val containerId = S.attr("container-id").toOption

    val space = spaces.get((id.toInt, containerId)).getOrElse(Seq.empty).sortBy(_.ord)


//    "*" #> <div class="space" data-xx-sid={id}>
//      {space.map { i =>
//
//        val snippet = Widgets.get(i.widget_id).snippet
//        val classSnippet = "lift:" + snippet
//        <div class="widget-init">
//          <div class={classSnippet} data-xx-wid={i.id} data-xx-widget={i.widget_id.toString}></div>
//        </div>
//      }}
//    </div>

  }
}
