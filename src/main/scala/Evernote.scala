import services.EvernoteAuth

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

  class StringCookie(name: String) {
    def apply[T](req: HttpRequest[T]): Option[String] = req match {
      case Cookies(cookies) => cookies(name).headOption.map(_.value)
    }
  }

  object TokenCookie extends StringCookie("token")

  object EnvironmentCookie extends StringCookie("environment")

  object AuthCookie {
    def unapply[T, E](req: HttpRequest[T]) =
      (EnvironmentCookie(req), TokenCookie(req)) match {
        case (Some(environment), Some(token)) => EvernoteAuth(environment, token)
        case _ => None
      }
  }

}
