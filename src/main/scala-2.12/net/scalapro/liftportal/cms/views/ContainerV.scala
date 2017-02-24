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

    def id = column[Option[Int]]("id")

    def name = column[String]("name")

    def description = column[Option[String]]("description", SqlType("TEXT"))

    def markup = column[String]("markup", SqlType("TEXT"))

    def kind = column[String]("kind")


    def * = (id, name, description, markup, kind) <>
      ((ContainerV.apply _).tupled, ContainerV.unapply)
  }

  val view = TableQuery[ContainersV]

  def createView():DBIO[Int] =

    sqlu"""
        create view containers_v as
        select * from containers where in_role()
      """
  def dropView():DBIO[Int] =

    sqlu"""
       drop view containers_v
      """


}
