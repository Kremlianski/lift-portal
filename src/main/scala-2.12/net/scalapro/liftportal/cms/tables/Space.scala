package net.scalapro.liftportal.cms.tables

import net.liftweb.http.FieldBinding.Default
import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.Nullable

case class Space (
                 id: Option[Int],
                 container_id: Option[Int],
                 flag: Option[String]
                 )

object Space {

  class Spaces(tag: Tag) extends Table[Space](tag, "spaces") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def container_id = column[Option[Int]]("container_id", Nullable, O.Default(None))

    def flag = column[Option[String]]("flag", Nullable, O.Default(None))


    def * = (id.?, container_id, flag) <> ((Space.apply _).tupled, Space.unapply)


    def container_fk = foreignKey("container_fk", container_id, Container.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }




  val table = TableQuery[Spaces]
}