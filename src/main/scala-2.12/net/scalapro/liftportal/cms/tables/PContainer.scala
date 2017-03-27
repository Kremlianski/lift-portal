package net.scalapro.liftportal.cms.tables

import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.Nullable

case class PContainer (
                        id: String,
                        container_id: Int,
                        page_id: Int,
                        p_container_id: String,
                        space_id: Int,
                        ord: Int,
                        container_class: Option[String]
                      )
object PContainer {

  class PContainers(tag: Tag) extends Table[PContainer](tag, "p_containers") {

    def id = column[String]("id", O.PrimaryKey)

    def container_id = column[Int]("container_id")

    def page_id = column[Int]("page_id")

    def p_container_id = column[String]("p_container_id")

    def space_id = column[Int]("space_id")

    def ord = column[Int]("ord")

    def container_class = column[Option[String]]("container_class", Nullable)


    def * = (id, container_id, page_id, p_container_id, space_id, ord, container_class) <>
      ((PContainer.apply _).tupled, PContainer.unapply)


    def container_fk = foreignKey("container_fk", container_id, Container.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    def page_fk = foreignKey("page_fk", page_id, Page.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    def space_fk = foreignKey("space_fk", space_id, Space.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    def p_container_fk = foreignKey("p_container_fk", p_container_id, PContainer.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  val table = TableQuery[PContainers]


}