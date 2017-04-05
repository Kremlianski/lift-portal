package net.scalapro.liftportal.cms.tables


import scala.collection.mutable.Map


case class Widget(
                   id: Int,
                   name: String,
                   snippet: String,
                   description: String,
                   kind: String
                 )

object Widgets {

  private val widgets: Map[Int, Widget] = Map.empty

  def add(widget: Widget): Unit = {
    widgets += (widget.id -> widget)
  }

  def get(): Map[Int, Widget] = widgets

  def get(key: Int): Widget = widgets(key)

  def register(): Unit = {
    add(Widget(1, "Text Element", "TextWidget", "Write any text", "Template"))
  }


}