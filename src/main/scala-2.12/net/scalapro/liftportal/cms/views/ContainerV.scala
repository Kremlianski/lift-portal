package net.scalapro.liftportal.cms.views

import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.SqlType

case class ContainerV(
                      id: Option[Int],
                      name: String,
                      description: Option[String],
                      markup: String,
                      kind: String
                    )

object ContainerV {

  class ContainersV(tag: Tag) extends Table[ContainerV](tag, "containers_v") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def description = column[Option[String]]("description", SqlType("TEXT"))

    def markup = column[String]("markup", SqlType("TEXT"))

    def kind = column[String]("kind")


    def * = (id.?, name, description, markup, kind) <>
      ((ContainerV.apply _).tupled, ContainerV.unapply)
  }

  val view = TableQuery[ContainersV]

  def createView():DBIO[Int] =

    sqlu"""
        CREATE VIEW containers_v AS
        SELECT * FROM containers WHERE in_role()
      """
  def dropView():DBIO[Int] =

    sqlu"""
       DROP VIEW containers_v
      """


}
