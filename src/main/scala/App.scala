import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import services.EvernoteService
import unfiltered.request._
import unfiltered.response._

import scala.collection.JavaConversions._

class App extends unfiltered.filter.Plan with Evernote {

  def intent = {
    case GET(Path("/notes")) & EvernoteAuth(auth) =>
      val service = EvernoteService.create(auth)
      val notes = service.findRecentNotes(0, 100)

      JsonContent ~> ResponseString(compact(render(
        "notes" -> notes.map(note => Map(
          "guid" -> note.getGuid,
          "title" -> note.getTitle,
          "created" -> note.getCreated.toString,
          "updated" -> note.getUpdated.toString
        ))
      )))

    case GET(Path(Seg("note" :: guid :: Nil))) & EvernoteAuth(auth) =>
      val service = EvernoteService.create(auth)
      val note = service.getNoteWithContent(guid)

      JsonContent ~> ResponseString(compact(render(
        ("guid" -> note.getGuid) ~
        ("title" -> note.getTitle) ~
        ("created" -> note.getCreated.toString) ~
        ("updated" -> note.getUpdated.toString) ~
        ("content" -> note.getContent) ~
        ("resources" -> note.getResources.map(resource =>
          ("guid" -> resource.getGuid) ~
          ("mime" -> resource.getMime)
        ))
      )))
  }

}
