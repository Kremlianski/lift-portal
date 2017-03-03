package net.scalapro.liftportal.util


object Space {

  def getSlug(id: Int):String = s"space_${id}"

  def getId(slag: String):Int = slag.replaceFirst("space_", "").toInt

}
