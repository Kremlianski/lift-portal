package net.scalapro.liftportal.cms.views

import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.SqlType



case class TemplateV (
                      id: Option[Int],
                      name: String,
                      description: Option[String],
                      markup: String
                    )

object TemplateV {

  class TemplatesV(tag: Tag) extends Table[TemplateV](tag, "templates_v") {


    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def description = column[Option[String]]("description", SqlType("TEXT"))

    def markup = column[String]("markup", SqlType("TEXT"))


    def * = (id.?, name, description, markup) <> ((TemplateV.apply _).tupled, TemplateV.unapply)

  }


  val view = TableQuery(new TemplatesV(_))

  def createView():DBIO[Int] =

    sqlu"""
        CREATE VIEW templates_v AS
        SELECT * FROM templates WHERE in_role()
      """
  def dropView():DBIO[Int] =

    sqlu"""
       DROP VIEW templates_v
      """
}
