package net.scalapro.liftportal.model.cms

import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.SqlType


/*

A template must produce markup that surrounds a page itself
It has only one container to store widgets

 */
case class PageTemplate (
                        id: Option[Long], //Template unique id
                        name: String, //The name
                        description: Option[String], //a description
                        markup: String // markup of the template
                        )


class PageTemplates(tag: Tag) extends Table[PageTemplate](tag, "page_templates") {



  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name", O.Default("New Template"))

  def description = column[Option[String]]("description", SqlType("TEXT"))
  def markup = column[String]("markup", SqlType("TEXT"))



  def * = (id.?, name, description, markup) <> (PageTemplate.tupled, PageTemplate.unapply)

}


object PageTemplatesTable extends TableQuery(new PageTemplates(_)) {

//  def create = {
//    this.schema.create
//  }
//  def drop = {
//    this.schema.drop
//  }
}