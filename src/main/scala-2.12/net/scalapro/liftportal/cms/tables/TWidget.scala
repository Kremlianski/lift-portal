package net.scalapro.liftportal.cms.tables

import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.{Nullable, SqlType}


case class TWidget (
                        id: String,
                        widget_id: Int,
                        template_id: Int,
                        space_id: Int,
                        ord: Int,
                        widget_class: Option[String],
                        params: Option[String]
                      )
object TWidget {

  class TWidgets(tag: Tag) extends Table[TWidget](tag, "t_widgets") {

    def id = column[String]("id", O.PrimaryKey)

    def widget_id = column[Int]("widget_id")

    def template_id = column[Int]("template_id")

    def space_id = column[Int]("space_id")


    def ord = column[Int]("ord")

    def widget_css_class = column[Option[String]]("widget_css_class", Nullable)

    def params = column[Option[String]]("params", SqlType("TEXT"))


    def * = (id, widget_id, template_id, space_id, ord, widget_css_class, params) <>
      ((TWidget.apply _).tupled, TWidget.unapply)


    def template_fk = foreignKey("template_fk", template_id, Template.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    def space_fk = foreignKey("space_fk", space_id, Space.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

  }


  val table = TableQuery[TWidgets]

}