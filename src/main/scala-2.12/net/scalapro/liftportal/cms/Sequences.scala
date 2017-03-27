package net.scalapro.liftportal.cms

import slick.jdbc.PostgresProfile.api._
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
}
