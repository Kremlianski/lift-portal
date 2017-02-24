package net.scalapro.liftportal.util


import net.liftweb._
import util.Props
import slick.jdbc.PostgresProfile.api._


object DB {
  def getDatabase: Database = {
    val user = Props.get("db.user") openOr ""
    val pass = Props.get("db.password") openOr ""
    val url = Props.get("db.url") openOr ""

    Database.forURL(url, user, pass)
  }
}