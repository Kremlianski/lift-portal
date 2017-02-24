package net.scalapro.liftportal.model.cms

import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.SqlType

case class Page (
                id: Option[Long], //Page id
                title: String, //<title>
                description: Option[String], //<meta type="description'>
                keywords: Option[String], //<meta type="keywords">
                pageType: Long, //ref to the type
                pageClass: Option[String], //<body class="">
                slag: String // the slag of the page
                )


class Pages(tag: Tag) extends Table[Page](tag, "pages") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def title = column[String]("title", O.Default("New Page"))

  def description = column[Option[String]]("description", SqlType("TEXT"))
  def keywords = column[Option[String]]("keywords", SqlType("TEXT"))

  def pageType = column[Long]("page_type", O.Default[Long](0))

  def pageClass = column[Option[String]]("page_class")

  def slag = column[String]("slag")


  def * = (id.?, title, description, keywords, pageType, pageClass, slag) <> (Page.tupled, Page.unapply)
  def template = foreignKey("temp_fk", pageType, PageTemplatesTable)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.SetDefault)
}


object PagesTable extends TableQuery(new Pages(_)) {

//  def create = {
//    this.schema.create
//  }
//  def drop = {
//    this.schema.drop
//  }
}