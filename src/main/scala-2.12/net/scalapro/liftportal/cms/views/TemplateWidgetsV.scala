package net.scalapro.liftportal.cms.views
import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.{Nullable, SqlType}
/**
  * Created by kreml on 10.04.2017.
  */
case class TemplateWidgetsV (
                      id: String,
                      widget_id: Int,
                      template_id: Int,
                      space_id: Int,
                      ord: Int,
                      widget_class: Option[String],
                      params: Option[String],
                      markup: String
                    )
object TemplateWidgetsV {
  class TemplatesWidgetsV(tag: Tag) extends Table[TemplateWidgetsV](tag, "template_widgets") {

    def t_widget = column[String]("t_widget", O.PrimaryKey)

    def widget = column[Int]("widget")

    def template = column[Int]("template")

    def space = column[Int]("space")


    def ord = column[Int]("ord")

    def css_class = column[Option[String]]("css_class", Nullable)

    def params = column[Option[String]]("params", SqlType("TEXT"))

    def markup = column[String]("markup", SqlType("TEXT"))


    def * = (t_widget, widget, template, space, ord, css_class, params, markup) <>
      ((TemplateWidgetsV.apply _).tupled, TemplateWidgetsV.unapply)
  }

  val view = TableQuery(new TemplatesWidgetsV(_))

  def createView():DBIO[Int] =

    sqlu"""
        CREATE VIEW template_widgets AS
        select w.id as t_widget, w.widget_id as widget, t.id as template,
         w.space_id as space, w.ord, w.widget_css_class as css_class, w.params,
          t.markup from templates as t left join t_widgets as w on t.id=w.template_id
      """
  def dropView():DBIO[Int] =

    sqlu"""
       DROP VIEW template_widgets
      """
}
