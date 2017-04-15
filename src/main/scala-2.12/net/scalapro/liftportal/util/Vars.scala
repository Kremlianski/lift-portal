package net.scalapro.liftportal.util

import net.liftweb.http.RequestVar
import net.scalapro.liftportal.view.TemplatePreView.WidgetsMap

/**
  * Created by kreml on 15.04.2017.
  */
object Vars {
  object spacesStorage extends RequestVar[WidgetsMap](Map.empty)
  object templateId extends RequestVar[String]("1")
}
