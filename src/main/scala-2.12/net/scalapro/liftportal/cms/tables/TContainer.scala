package net.scalapro.liftportal.cms.tables

import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.Nullable

case class TContainer (
                    id: Option[Int],
                    container_id: Int,
                    template_id: Int,
                    t_container_id: Int,
                    space_id: Int,
                    ord: Int,
                    container_class: Option[String]
                  )

object TContainer {

  class TContainers(tag: Tag) extends Table[TContainer](tag, "t_containers") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def container_id = column[Int]("container_id")

    def template_id = column[Int]("template_id")

    def t_container_id = column[Int]("t_container_id")

    def space_id = column[Int]("space_id")

    def ord = column[Int]("ord")

    def container_class = column[Option[String]]("container_class", Nullable)


    def * = (id.?, container_id, template_id, t_container_id, space_id, ord, container_class) <>
      ((TContainer.apply _).tupled, TContainer.unapply)

    def container_fk = foreignKey("container_fk", container_id, TContainer.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    def template_fk = foreignKey("template_fk", template_id, Template.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    def space_fk = foreignKey("space_fk", space_id, Space.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    def t_container_fk = foreignKey("t_container_fk", t_container_id, PContainer.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }


  val table = TableQuery[TContainers]

}