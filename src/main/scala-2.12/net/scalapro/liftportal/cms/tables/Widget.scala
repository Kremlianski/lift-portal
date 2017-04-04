package net.scalapro.liftportal.cms.tables
import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlProfile.ColumnOption.SqlType


case class Widget (
                       id: Option[Int],
                       name: String,
                       description: Option[String],
                       kind: String
                     )

