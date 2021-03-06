package net.scalapro.liftportal.cms.tables


import scala.collection.mutable.Map


case class Widget(
                   id: Int,
                   name: String,
                   snippet: String,
                   kind: String,
                   description: String = "",
                   jsFunc: String = ""
                 )

object Widgets {

  private val widgets: Map[Int, Widget] = Map.empty

  def add(widget: Widget): Unit = {
    widgets += (widget.id -> widget)
  }

  def get(): Seq[Widget] = widgets.values.toSeq

  def get(key: Int): Widget = widgets(key)

  def register(): Unit = {
    add(Widget(1, "Text Element", "TextWidget", "Write any text", "Template"))
    add(Widget(2, "Text Element 2", "MenuWidget", "Place your menu", "Template", "menuInit"))
  }

}