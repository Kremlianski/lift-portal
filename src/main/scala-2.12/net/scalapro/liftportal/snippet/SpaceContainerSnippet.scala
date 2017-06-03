package net.scalapro.liftportal.snippet

import net.liftweb.http.S
import net.liftweb.util.Helpers._
import net.scalapro.liftportal.util.Vars.{containersStorage, markupStorage}

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

    println(space)
    "*" #> <div class="space" data-xx-sid={id}>
      {space.map { i =>
        val markup = markups.get(i.container_id)
        println(markup)
        val classSnippet = "lift:ContainerSnippet?id="+containerId.getOrElse("")
        <div class="container-init">
          <div class={classSnippet} data-xx-cid={i.id.toString} data-xx-container={i.container_id.toString}></div>
        </div>
      }}
    </div>

  }
}
