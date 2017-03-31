package net.scalapro.liftportal.snippet

import net.liftweb.http.S

import scala.xml.{NodeSeq, Unparsed}
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.views.TemplateV
import net.scalapro.liftportal.util.DB

import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
//import net.scalapro.liftportal.util.FutureBinds._
import scala.util.{Failure, Success}

import scala.xml.{Elem, Node, XML}
/**
  * Created by kreml on 21.03.2017.
  */
class SpaceSnippet {



  def render = {
    val id = S.attr("id").openOr("0")
    "*" #> <div class="space" data-xx-sid={id}></div>
  }
}
