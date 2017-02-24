package net.scalapro.liftportal.cms.tables

import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.SqlType



case class Template (
                          id: Option[Int],
                          name: String,
                          description: Option[String],
                          markup: String
                        )

object Template {

  class Templates(tag: Tag) extends Table[Template](tag, "templates") {


    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name", O.Default("New Template"))

    def description = column[Option[String]]("description", SqlType("TEXT"))

    def markup = column[String]("markup", SqlType("TEXT"))


    def * = (id.?, name, description, markup) <> ((Template.apply _).tupled, Template.unapply)

}


  val table = TableQuery(new Templates(_))

}