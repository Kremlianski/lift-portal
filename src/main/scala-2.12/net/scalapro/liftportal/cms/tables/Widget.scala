package net.scalapro.liftportal.cms.tables
import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.SqlType


case class Widget (
                       id: Option[Int],
                       name: String,
                       description: Option[String],
                       kind: String
                     )

object Widget {

  class Widgets(tag: Tag) extends Table[Widget](tag, "widgets") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name", O.Default("New Container"))

    def description = column[Option[String]]("description", SqlType("TEXT"))

    def kind = column[String]("kind")

    def * = (id.?, name, description, kind) <> ((Widget.apply _).tupled, Widget.unapply)
  }

  val table = TableQuery[Widgets]

}