package net.scalapro.liftportal.cms

import slick.jdbc.PostgresProfile.api._

object SQLFunctions {
  def create: DBIO[Int] =
    sqlu"""
       CREATE OR REPLACE FUNCTION in_role() RETURNS boolean AS $$f$$
       BEGIN
           RETURN 1;
       END;
       $$f$$ LANGUAGE plpgsql
      """

  def drop: DBIO[Int] =
    sqlu"""
      drop function if exists in_role()
      """
}
