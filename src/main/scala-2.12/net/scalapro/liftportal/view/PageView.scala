package net.scalapro.liftportal.view

import scala.xml.NodeSeq
import net.scalapro.liftportal.util.Vars.templateId

/**
  * Created by kreml on 17.04.2017.
  */
object PageView {

  def render(): NodeSeq = {
    templateId("1")

    <lift:surround with="cms/page-template-preview" at="xx-page">
      <div>The Page content will appear here!</div>
    </lift:surround>
  }

}
