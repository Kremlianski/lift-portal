package net.scalapro.liftportal.cms.tables

import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.{Nullable, SqlType}


case class TWidget (
                        id: Option[Int],
                        widget_id: Int,
                        template_id: Int,
                        space_id: Int,
                        t_container_id: Int,
                        params: Option[String],
                        ord: Int,
                        widget_class: Option[String]
                      )
object TWidget {

  class TWidgets(tag: Tag) extends Table[TWidget](tag, "t_widgets") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def widget_id = column[Int]("widget_id")

    def template_id = column[Int]("template_id")

    def space_id = column[Int]("space_id")

    def t_container_id = column[Int]("t_container_id")

    def ord = column[Int]("ord")

    def widget_class = column[Option[String]]("container_class", Nullable)

    def params = column[Option[String]]("params", SqlType("TEXT"))


    def * = (id.?, widget_id, template_id, space_id, t_container_id, params, ord, widget_class) <>
      ((TWidget.apply _).tupled, TWidget.unapply)

    def widget_fk = foreignKey("widget_fk", widget_id, Widget.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    def template_fk = foreignKey("template_fk", template_id, Template.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    def space_fk = foreignKey("space_fk", space_id, Space.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    def t_container_fk = foreignKey("t_container_fk", t_container_id, PContainer.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }


  val table = TableQuery[TWidgets]

}