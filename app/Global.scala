import controllers.Application.Broadcastor
import org.joda.time.DateTime
import play.api.libs.concurrent.Akka
import play.api.{Play, Application, GlobalSettings}
import scala.concurrent.duration._
import play.api.Play.current

object Global extends GlobalSettings {

  override def onStart(app: Application) = {
    import play.api.libs.concurrent.Execution.Implicits._
    Akka.system.scheduler.schedule(
      0 seconds,
      1 second,
      Broadcastor.actor,
      controllers.Application.Tick
    )
  }

}
