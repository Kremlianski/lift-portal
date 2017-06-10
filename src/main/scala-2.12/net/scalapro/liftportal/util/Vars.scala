package net.scalapro.liftportal.util

import net.liftweb.http.RequestVar
import net.scalapro.liftportal.cms.views.PItemV
import net.scalapro.liftportal.view.TemplatePreView.WidgetsMap

/**
  * Created by kreml on 15.04.2017.
  */
object Vars {
  object spacesStorage extends RequestVar[WidgetsMap](Map.empty)
  object containersStorage extends RequestVar[Map[(Int, Option[String]), Seq[PItemV]]](Map.empty)
  object markupStorage extends RequestVar[Map[Int, String]](Map.empty)

  object templateId extends RequestVar[String]("1")
  object pageId extends RequestVar[String]("1")


  object isContainerEdit extends RequestVar[Boolean](false)


}
