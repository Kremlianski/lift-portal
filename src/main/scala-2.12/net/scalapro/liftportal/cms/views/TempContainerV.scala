package net.scalapro.liftportal.cms.views

import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.SqlType

/**
  * Created by kreml on 29.03.2017.
  */
case class TempContainerV(
                           id: Int,
                           markup: String,
                           tempId: Int
                         )
object TempContainerV {
  class TempContainersV(tag: Tag) extends Table[TempContainerV](tag, "temp_containers_v") {
    def id = column[Int]("id", O.PrimaryKey)

    def markup = column[String]("markup", SqlType("TEXT"))

    def tempId = column[Int]("temp_id")


    def * = (id, markup, tempId) <> ((TempContainerV.apply _).tupled, TempContainerV.unapply)
  }

  val view = TableQuery[TempContainersV]

  def createView():DBIO[Int] =

    sqlu"""
        CREATE VIEW temp_containers_v AS
        SELECT c.id AS id, c.markup AS markup, (SELECT nextval('universal_seq')) AS temp_id FROM containers c
      """
  def dropView():DBIO[Int] =

    sqlu"""
       DROP VIEW temp_containers_v
      """
}
