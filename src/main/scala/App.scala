import com.evernote.auth.{EvernoteAuth, EvernoteService}
import com.evernote.clients.ClientFactory
import com.evernote.edam.`type`.NoteSortOrder
import com.evernote.edam.notestore.NoteFilter
import com.evernote.edam.userstore.Constants._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import unfiltered.request._
import unfiltered.response._

import scala.collection.JavaConversions._

class App extends unfiltered.filter.Plan {

  object TokenHeader extends StringHeader("X-EN-Token")
  object EnvironmentHeader extends StringHeader("X-EN-Environment")

  def intent = {
    case req @ GET(Path("/notes")) =>
      TokenHeader(req) match {
        case Some(token) =>
          val environment = EnvironmentHeader(req) match {
            case Some("sandbox")    => EvernoteService.SANDBOX
            case Some("production") => EvernoteService.PRODUCTION
            case _                  => EvernoteService.SANDBOX
          }

          val auth = new EvernoteAuth(environment, token)
          val factory = new ClientFactory(auth)

          val userStore = factory.createUserStoreClient()
          userStore.checkVersion(getClass.getName, EDAM_VERSION_MAJOR, EDAM_VERSION_MINOR) match {
            case true =>
              val noteStore = factory.createNoteStoreClient()

              val filter = new NoteFilter()
              filter.setOrder(NoteSortOrder.CREATED.getValue)
              filter.setAscending(false)
              val notes = noteStore.findNotes(filter, 0, 100).getNotes

              JsonContent ~> ResponseString(compact(render(
                "notes" -> notes.map(note => Map(
                  "guid" -> note.getGuid,
                  "title" -> note.getTitle,
                  "created" -> note.getCreated.toString,
                  "updated" -> note.getUpdated.toString
                ))
              )))

            case false =>
              InternalServerError ~> ResponseString("Incompatible protocol version")
          }

        case None =>
          NotFound
      }
  }

}
