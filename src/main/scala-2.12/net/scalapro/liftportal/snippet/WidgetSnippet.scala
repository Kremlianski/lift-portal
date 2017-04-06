package net.scalapro.liftportal.snippet

import net.liftweb.http.S
import net.scalapro.liftportal.cms.tables._
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.Sequences

/**
  * Created by kreml on 05.04.2017.
  */
class WidgetSnippet {

  def render = {
    val id = S.attr("id").openOr("0")

    val snippet = Widgets.get(id.toInt).snippet
    val classSnippet = "lift:" + snippet

    "*" #> <div class={classSnippet}></div> andThen
    "div [class+]" #> "xx-new" &
    "div [data-xx-wid]" #> {Sequences.newUniversalSeq} &
    "div [data-xx-widget]" #> {id}
  }
}
