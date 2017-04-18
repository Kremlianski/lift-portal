package net.scalapro.liftportal.cms.views


import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.SqlType

case class PageV(
                 id: Option[Int],
                 template_id: Int,
                 title: String,
                 description: Option[String],
                 keywords: Option[String],
                 pageType: String,
                 pageClass: Option[String]
               )

object PageV {

  class PagesV(tag: Tag) extends Table[PageV](tag, "pages_v") {

       def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def template_id = column[Int]("template_id")


    def title = column[String]("title")

    def description = column[Option[String]]("description", SqlType("TEXT"))

    def keywords = column[Option[String]]("keywords", SqlType("TEXT"))

    def pageType = column[String]("page_type")

    def pageClass = column[Option[String]]("page_class")



    def * = (id.?, template_id, title, description, keywords, pageType, pageClass) <>
      ((PageV.apply _).tupled, PageV.unapply)

  }

  val view = TableQuery[PagesV]

  def createView():DBIO[Int] =

      sqlu"""
        CREATE VIEW pages_v AS
        SELECT * FROM pages WHERE in_role()
      """
  def dropView():DBIO[Int] =

    sqlu"""
       DROP VIEW pages_v
      """
}

