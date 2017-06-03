package net.scalapro.liftportal.snippet

import net.liftweb.http.S
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.cms.tables.Widgets
import net.scalapro.liftportal.util.Vars.spacesStorage

/**
  * Created by kreml on 21.03.2017.
  */
class ContainerSnippet {


  def render = {

    "*" #> <div class="container"></div>

  }
}
