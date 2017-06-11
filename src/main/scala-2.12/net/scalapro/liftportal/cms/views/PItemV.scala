package net.scalapro.liftportal.cms.views

import slick.sql.SqlProfile.ColumnOption.{Nullable, SqlType}
import slick.jdbc.PostgresProfile.api._
/**
  * Created by kreml on 08.06.2017.
  */
sealed trait PItemV {
  def ord: Int
  def p_container_id: Option[String]
  def space_id: Int
}


case class PContainerV  (
                         id: String,
                         container_id: Int,
                         page_id: Int,
                         p_container_id: Option[String],
                         space_id: Int,
                         ord: Int,
                         container_class: Option[String]
                       ) extends PItemV


case class PWidgetV (
                      id: String,
                      widget_id: Int,
                      page_id: Int,
                      space_id: Int,
                      p_container_id: Option[String],
                      params: Option[String],
                      ord: Int,
                      widget_class: Option[String]
                    ) extends PItemV


object PWidgetV {
  class TWidgetsV(tag: Tag) extends Table[PWidgetV](tag, "p_widgets_v") {

    def id = column[String]("id", O.PrimaryKey)

    def widget_id = column[Int]("widget_id")

    def page_id = column[Int]("page_id")

    def space_id = column[Int]("space_id")

    def p_container_id = column[Option[String]]("p_container_id")

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


object PContainerV {

  class PContainersV(tag: Tag) extends Table[PContainerV](tag, "p_containers_v") {

    def id = column[String]("id", O.PrimaryKey)

    def container_id = column[Int]("container_id")

    def page_id = column[Int]("page_id")

    def p_container_id = column[Option[String]]("p_container_id")

    def space_id = column[Int]("space_id")

    def ord = column[Int]("ord")

    def container_class = column[Option[String]]("container_class", Nullable)


    def * = (id, container_id, page_id, p_container_id, space_id, ord, container_class) <>
      ((PContainerV.apply _).tupled, PContainerV.unapply)

  }

  val view = TableQuery[PContainersV]

  def createView():DBIO[Int] =

    sqlu"""
        CREATE VIEW p_containers_v AS
        SELECT * FROM p_containers WHERE in_role()
      """
  def dropView():DBIO[Int] =

    sqlu"""
       DROP VIEW p_containers_v
      """


}
