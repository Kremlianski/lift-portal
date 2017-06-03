package net.scalapro.liftportal.snippet

import net.liftweb.http.S
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.tables.Widgets
import net.scalapro.liftportal.util.Vars.{isContainerEdit, spacesStorage}

/**
  * Created by kreml on 21.03.2017.
  */
class SpaceSnippet {


  def render = isContainerEdit.is match {
    case false => commonRender
    case true => containerEditRender
  }
  private def commonRender = {

    val spaces = spacesStorage.is
    val id = S.attr("id").openOr("0")

    val space = spaces.get(id.toInt).getOrElse(Seq.empty).sortBy(_.ord)


    "*" #> <div class="space" data-xx-sid={id}>
      {space.map { i =>

        val snippet = Widgets.get(i.widget_id).snippet
        val classSnippet = "lift:" + snippet
        <div class="widget-init">
          <div class={classSnippet} data-xx-wid={i.id} data-xx-widget={i.widget_id.toString}></div>
        </div>
      }}
    </div>

  }

  private def containerEditRender = {

    val id = S.attr("id").openOr("0")
    val classSnippet = "lift:SpaceContainerSnippet?id=" + id
    "*" #> <div class={classSnippet}></div>
  }
}
