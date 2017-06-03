package net.scalapro.liftportal.snippet

import net.liftweb.common.{Box, Empty}
import net.liftweb.http.S
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.util.Vars.{containersStorage, markupStorage}

import scala.xml.{NodeSeq, XML, Elem}

/**
  * Created by kreml on 21.03.2017.
  */
class SpaceContainerSnippet {


  def render = {
    val spaces = containersStorage.is
    val markups = markupStorage.is
    val id = S.attr("id").openOr("0")
    val containerId = S.attr("container-id").toOption

    val space = spaces.get((id.toInt, containerId)).getOrElse(Seq.empty).sortBy(_.ord)

    "*" #> <div class="space" data-xx-sid={id}>
      {space.map { i =>
        val markup = XML.loadString(markups.get(i.container_id).getOrElse(""))

//        val classSnippet = "lift:ContainerSnippet?id="+containerId.getOrElse("")
        var lift = ""
        val transform = {
          "data-xx-role=c [data-xx-cid]" #> {
            i.id
          } andThen
            "data-xx-role=c [data-xx-container]" #> {i.container_id} andThen
            "data-xx-role=c [data-xx-role]" #> (Empty: Box[String])
        } andThen
          ".space [data-xx-c]" #> {i.id} andThen
          ".space" #> ((n: NodeSeq) => {
            lift = n.asInstanceOf[Elem].attribute("lift").getOrElse("").toString
            n
          }) andThen
          ".space [lift]" #> {lift + ";container-id=" + i.id}


        //        <div class="container-init">
//          <div class={classSnippet} data-xx-cid={i.id.toString} data-xx-container={i.container_id.toString}></div>
//        </div>
        val r = transform(markup)
        println(r)
        r
      }}
    </div>

  }




}
