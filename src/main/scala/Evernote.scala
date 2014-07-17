import com.evernote.auth._
import scala.util.control.Exception._
import unfiltered.request._

trait Evernote {

  object TokenHeader extends StringHeader("X-EN-Token")

  object EnvironmentHeader extends StringHeader("X-EN-Environment")

  object EvernoteAuth {
    def unapply[T](req: HttpRequest[T]) =
      (EnvironmentHeader(req), TokenHeader(req)) match {
        case (Some(environment), Some(token)) =>
          catching(classOf[IllegalArgumentException]) opt
            (EvernoteService.valueOf(environment), token)

        case _ => None
      }
  }

}
