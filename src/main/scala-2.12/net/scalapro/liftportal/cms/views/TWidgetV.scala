package net.scalapro.liftportal.cms.views
import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.{Nullable, SqlType}
/**
  * Created by kreml on 08.04.2017.
  */

case class TWidgetV (
                      id: String,
                      widget_id: Int,
                      template_id: Int,
                      space_id: Int,
                      ord: Int,
                      widget_class: Option[String],
                      params: Option[String]
                    )
object TWidgetV {
  class TWidgetsV(tag: Tag) extends Table[TWidgetV](tag, "t_widgets_v") {

    def id = column[String]("id", O.PrimaryKey)

    def widget_id = column[Int]("widget_id")

    def template_id = column[Int]("template_id")

    def space_id = column[Int]("space_id")


    def ord = column[Int]("ord")

    def widget_css_class = column[Option[String]]("widget_css_class", Nullable)

    def params = column[Option[String]]("params", SqlType("TEXT"))


    def * = (id, widget_id, template_id, space_id, ord, widget_css_class, params) <>
      ((TWidgetV.apply _).tupled, TWidgetV.unapply)
  }

  val view = TableQuery(new TWidgetsV(_))

  def createView():DBIO[Int] =

    sqlu"""
        CREATE VIEW t_widgets_v AS
        SELECT * FROM t_widgets WHERE in_role()
      """
  def dropView():DBIO[Int] =

    sqlu"""
       DROP VIEW t_widgets_v
      """
}
