package net.scalapro.liftportal.cms

import net.scalapro.liftportal.util.DB
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by kreml on 27.03.2017.
  */
object Sequences {
  def create: DBIO[Int] =
    sqlu"""
       CREATE SEQUENCE IF NOT EXISTS universal_seq START 1
      """

  def drop: DBIO[Int] =
    sqlu"""
      DROP SEQUENCE IF EXISTS universal_seq
      """

  def newUniversalSeq():Int = {
    val db = DB.getDatabase
    try {
      val action =sql"SELECT nextval('universal_seq')".as[(Int)] //The Query


      val result = Await.result(
        db.run(action) // Future[Seq[TempContainerV]]
          .map(i => i.head)
        , Duration(2, "second")
      )
      result

    }
    finally {
      db.close
    }
  }
}
