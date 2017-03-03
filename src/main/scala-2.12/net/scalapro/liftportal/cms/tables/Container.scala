package net.scalapro.liftportal.cms.tables

import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.SqlType

case class Container(
                      id: Option[Int],
                      name: String,
                      description: Option[String],
                      markup: String,
                      kind: String
                    )

object Container {


  class Containers(tag: Tag) extends Table[Container](tag, "containers") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name", O.Default("New Container"))

    def description = column[Option[String]]("description", SqlType("TEXT"))

    def markup = column[String]("markup", SqlType("TEXT"))

    def kind = column[String]("kind", O.Default("cms"))


    def * = (id.?, name, description, markup, kind) <> ((Container.apply _).tupled, Container.unapply)
  }

  val table = TableQuery[Containers]


}
