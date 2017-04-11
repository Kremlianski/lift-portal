package net.scalapro.liftportal.snippet

import net.liftweb.http.S
import net.scalapro.liftportal.view.TemplateView._
import net.scalapro.liftportal.cms.views.TWidgetV

import scala.xml.{NodeSeq, Unparsed}
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.tables.Widgets
import net.scalapro.liftportal.cms.views.TemplateV
import net.scalapro.liftportal.util.DB

import scala.concurrent.Await
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
//import net.scalapro.liftportal.util.FutureBinds._
import scala.util.{Failure, Success}

import scala.xml.{Elem, Node, XML, Text}
/**
  * Created by kreml on 21.03.2017.
  */
class SpaceSnippet {



  def render = {
    val spaces = spacesStorage.is
    val id = S.attr("id").openOr("0")

    val space = spaces.get(id.toInt).getOrElse(Map.empty).asInstanceOf[Seq[TWidgetV]]




    "*" #> <div class="space" data-xx-sid={id}>

    </div>

  }
}
