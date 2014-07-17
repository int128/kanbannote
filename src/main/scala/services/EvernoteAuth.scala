package services

import scala.util.control.Exception._

case class EvernoteAuth(environment: com.evernote.auth.EvernoteService, token: String)

object EvernoteAuth {

  def apply(environment: String, token: String): Option[EvernoteAuth] =
    catching(classOf[IllegalArgumentException]) opt
      EvernoteAuth(com.evernote.auth.EvernoteService.valueOf(environment), token)

}
