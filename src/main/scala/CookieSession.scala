import java.util.UUID

import unfiltered.Cookie
import unfiltered.request._

trait CookieSession extends Memcache {

  private lazy val cookieKey = "s"

  object SessionCookie {
    def unapply[T, E](req: HttpRequest[T]): Option[E] =
      req match {
        case Cookies(cookies) =>
          cookies(cookieKey) match {
            case Some(cookie) =>
              val sessionId = cookie.value
              getCache(sessionId)

            case None => None
          }
      }

    def apply[E](value: E): Cookie = {
      val sessonId = UUID.randomUUID().toString
      putCache(sessonId, value)
      Cookie(
        cookieKey,
        sessonId,
        None,
        None,
        None,
        None
      )
    }
  }

}
