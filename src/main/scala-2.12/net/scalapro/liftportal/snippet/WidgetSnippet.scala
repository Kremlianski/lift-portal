package net.scalapro.liftportal.snippet

import net.liftweb.http.S
import net.scalapro.liftportal.cms.tables._
import net.liftweb.util.Helpers._

/**
  * Created by kreml on 05.04.2017.
  */
class WidgetSnippet {

  def render = {
    val id = S.attr("id").openOr("0")

    val snippet = Widgets.get(1).snippet
    val classSnippet = "lift:" + snippet

    "*" #> <div class={classSnippet}></div>
  }
}
