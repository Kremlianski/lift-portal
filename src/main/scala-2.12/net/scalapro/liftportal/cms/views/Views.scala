package net.scalapro.liftportal.cms.views

import slick.jdbc.PostgresProfile.api._


object Views {
  def create =
    PageV.createView() >>
      ContainerV.createView() >>
      PContainerV.createView() >>
      TemplateV.createView() >>
      TempContainerV.createView() >>
      TWidgetV.createView() >>
      TemplateWidgetsV.createView()

  def drop =
    PageV.dropView() >>
      ContainerV.dropView() >>
      PContainerV.dropView() >>
      TemplateV.dropView() >>
      TempContainerV.dropView() >>
      TWidgetV.dropView() >>
      TemplateWidgetsV.dropView()
}

