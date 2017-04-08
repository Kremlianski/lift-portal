package net.scalapro.liftportal.cms.views

import slick.jdbc.PostgresProfile.api._


object Views {
  def create =
    PageV.createView() >>
      ContainerV.createView() >>
      TemplateV.createView() >>
      TempContainerV.createView() >>
      TWidgetV.createView()

  def drop =
    PageV.dropView() >>
      ContainerV.dropView() >>
      TemplateV.dropView() >>
      TempContainerV.dropView() >>
      TWidgetV.dropView()
}

