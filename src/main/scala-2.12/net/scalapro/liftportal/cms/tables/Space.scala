package net.scalapro.liftportal.cms.tables


import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.Nullable

case class Space (
                 id: Option[Int],
                 container_id: Option[Int],
                 template_id: Option[Int],
                 page_id: Option[Int]

                 )

object Space {

  class Spaces(tag: Tag) extends Table[Space](tag, "spaces") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def container_id = column[Option[Int]]("container_id", Nullable, O.Default(None))

    def template_id = column[Option[Int]]("template_id", Nullable, O.Default(None))

    def page_id = column[Option[Int]]("page_id", Nullable, O.Default(None))




    def * = (id.?, container_id, template_id, page_id) <> ((Space.apply _).tupled, Space.unapply)


    def container_fk = foreignKey("container_fk", container_id, Container.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    def template_fk = foreignKey("template_fk", template_id, Template.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    def page_fk = foreignKey("page_fk", page_id, Page.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)


    def container_index = index("container_index", container_id)

    def template_index = index("template_index", template_id)

    def page_index = index("page_index", page_id)

  }




  val table = TableQuery[Spaces]
}