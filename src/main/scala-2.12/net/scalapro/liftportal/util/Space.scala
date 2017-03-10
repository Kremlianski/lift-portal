package net.scalapro.liftportal.util


object SpaceUtil {

  def getId(id: Int):String = s"space_${id}"

  def getId(id: String):Int = id.replaceFirst("space_", "").toInt

  def getMarkup(id: Int):String = s"<div class='exxo-space' id='space_${id}'></div>"
}
