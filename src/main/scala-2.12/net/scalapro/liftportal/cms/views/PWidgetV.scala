package net.scalapro.liftportal.cms.views

/**
  * Created by kreml on 08.06.2017.
  */

import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.{Nullable, SqlType}


case class PWidgetV (
                      id: String,
                      widget_id: Int,
                      page_id: Int,
                      space_id: Int,
                      p_container_id: String,
                      params: Option[String],
                      ord: Int,
                      widget_class: Option[String]
                    )
object PWidgetV {
  class TWidgetsV(tag: Tag) extends Table[PWidgetV](tag, "p_widgets_v") {

    def id = column[String]("id", O.PrimaryKey)

    def widget_id = column[Int]("widget_id")

    def page_id = column[Int]("page_id")

    def space_id = column[Int]("space_id")

    def p_container_id = column[String]("p_container_id")

    def ord = column[Int]("ord")

    def widget_class = column[Option[String]]("container_class", Nullable)

    def params = column[Option[String]]("params", SqlType("TEXT"))


    def * = (id, widget_id, page_id, space_id, p_container_id, params, ord, widget_class) <>
      ((PWidgetV.apply _).tupled, PWidgetV.unapply)
  }

  val view = TableQuery(new TWidgetsV(_))

  def createView():DBIO[Int] =

    sqlu"""
        CREATE VIEW p_widgets_v AS
        SELECT * FROM p_widgets WHERE in_role()
      """
  def dropView():DBIO[Int] =

    sqlu"""
       DROP VIEW p_widgets_v
      """
}

