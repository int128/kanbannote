import com.evernote.auth.{EvernoteAuth, EvernoteService}
import com.evernote.clients.ClientFactory
import com.evernote.edam.userstore.Constants._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import unfiltered.request._
import unfiltered.response._

import scala.collection.JavaConversions._

class App extends unfiltered.filter.Plan {

  object TokenHeader extends StringHeader("X-EN-Token")

  def intent = {
    case req @ GET(Path("/notes")) =>
      val token = ""
      val auth = new EvernoteAuth(EvernoteService.SANDBOX, token)
      val factory = new ClientFactory(auth)

      val userStore = factory.createUserStoreClient()
      userStore.checkVersion(getClass.getName, EDAM_VERSION_MAJOR, EDAM_VERSION_MINOR) match {
        case true =>
          val noteStore = factory.createNoteStoreClient()
          val notebooks = noteStore.listNotebooks()
          val result = notebooks.map(_.getName)

          JsonContent ~> ResponseString(compact(render(
            result
          )))

        case false =>
          InternalServerError ~> ResponseString("Incompatible protocol version")
      }

//      TokenHeader(req) match {
//        case Some(token) =>
//
//        case None =>
//          NotFound
//      }
  }

}
