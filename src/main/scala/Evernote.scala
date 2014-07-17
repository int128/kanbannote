
import models.EvernoteAuth
import unfiltered.request._

trait Evernote {

  object TokenHeader extends StringHeader("X-EN-Token")

  object EnvironmentHeader extends StringHeader("X-EN-Environment")

  object AuthHeader {
    def unapply[T](req: HttpRequest[T]) =
      (EnvironmentHeader(req), TokenHeader(req)) match {
        case (Some(environment), Some(token)) => EvernoteAuth(environment, token)
        case _ => None
      }
  }

}
