package net.scalapro.liftportal.util

import scala.concurrent.{Future,Await,ExecutionContext}
import scala.concurrent.duration.Duration
import scala.xml.NodeSeq

import net.liftweb.actor.LAFuture
import net.liftweb.http.S
import net.liftweb.util._
import Helpers._

object FutureBinds {
  private def futureTransform[FutureType, T](innerTransform: CanBind[T], resolveFuture: (FutureType)=>T): CanBind[FutureType] = new CanBind[FutureType] {
    def apply(future: => FutureType)(ns: NodeSeq): Seq[NodeSeq] = {
      val concreteFuture = future
      val snippetId = s"lazySnippet${Helpers.nextNum}"

      S.mapSnippet(snippetId, { ns: NodeSeq =>
        innerTransform(resolveFuture(concreteFuture))(ns).flatten
      })

      <lift:lazy-load>{("^ [data-lift]" #> snippetId) apply ns}</lift:lazy-load>
    }
  }

  implicit def futureTransform[T](implicit innerTransform: CanBind[T], executionContext: ExecutionContext): CanBind[Future[T]] = {
    futureTransform[Future[T],T](innerTransform, (future) => Await.result(future, Duration.Inf))
  }

  implicit def lafutureTransform[T](implicit innerTransform: CanBind[T]): CanBind[LAFuture[T]] = {
    futureTransform[LAFuture[T],T](innerTransform, (future) => future.get)
  }
}