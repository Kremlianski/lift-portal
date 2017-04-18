package net.scalapro.liftportal.cms.tables

import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.SqlType

case class Page(
                 id: Option[Int],
                 template_id: Int,
                 title: String,
                 description: Option[String],
                 keywords: Option[String],
                 pageType: String,
                 pageClass: Option[String]
               )

object Page {

  class Pages(tag: Tag) extends Table[Page](tag, "pages") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def template_id = column[Int]("template_id", O.Default(1))


    def title = column[String]("title", O.Default("New Page"))

    def description = column[Option[String]]("description", SqlType("TEXT"))

    def keywords = column[Option[String]]("keywords", SqlType("TEXT"))

    def pageType = column[String]("page_type")

    def pageClass = column[Option[String]]("page_class")




    def * = (id.?, template_id, title, description, keywords, pageType, pageClass) <> ((Page.apply _).tupled, Page.unapply)

    def template_fk = foreignKey("template_fk", template_id, Template.table)(_.id,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.SetDefault)
  }

  val table = TableQuery[Pages]

}
