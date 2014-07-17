import unfiltered.request._

trait CookieSupport {

  class StringCookie(name: String) {
    def apply[T](req: HttpRequest[T]): Option[String] = req match {
      case Cookies(cookies) => cookies(name).headOption.map(_.value)
    }
  }

}
