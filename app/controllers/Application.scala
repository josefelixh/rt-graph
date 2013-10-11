package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee.{Iteratee, Concurrent}
import akka.actor.{Props, Actor}
import play.api.libs.concurrent.Akka
import play.api.libs.json._
import org.joda.time.DateTime

object Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index("Your new websockets application is ready."))
  }

  def ws = WebSocket.using[JsValue] { implicit request =>
    import Broadcastor._
    (iteratee, enumerator)
  }

  object Broadcastor {
    val iteratee = Iteratee.ignore[JsValue]
    val (enumerator, channel) = Concurrent.broadcast[JsValue]

    private val iterator = Iterator.iterate(0.0) { _ + 0.5 }
    private def next(ts: Long) = {
      Json.obj(
        "ts" -> ts,
        "val" -> Json.arr(iterator.next(), Math.random())
      )
    }

    lazy val actor = {
      import play.api.Play.current
      Akka.system.actorOf(Props[Broadcastor])
    }

  }

  case object Tick

  class Broadcastor extends Actor {
    import Broadcastor._

    override def receive = {
      case Tick => channel.push(next(DateTime.now().getMillis))
    }

  }

}