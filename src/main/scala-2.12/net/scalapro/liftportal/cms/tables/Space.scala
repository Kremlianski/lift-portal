package net.scalapro.liftportal.cms.tables

import slick.jdbc.PostgresProfile.api._

case class Space (
                 id: Option[Int],
                 container_id: Int
                 )

object Space {

  class Spaces(tag: Tag) extends Table[Space](tag, "spaces") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def container_id = column[Int]("container_id")


    def * = (id.?, container_id) <> ((Space.apply _).tupled, Space.unapply)


    def container_fk = foreignKey("container_fk", container_id, Container.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }




  val table = TableQuery[Spaces]
}