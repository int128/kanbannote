import models._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import services.EvernoteService.ENML
import services._
import unfiltered.filter.request._
import unfiltered.request._
import unfiltered.response._

import scala.collection.JavaConversions._

class App extends unfiltered.filter.Plan with Evernote {

  def intent = {
    case GET(Path("/notes")) & AuthHeader(auth) =>
      val notes = EvernoteService(auth).findRecentNotes(0, 100)
      JsonContent ~>
        JsonResponse(
          notes.map(note =>
            ("guid" -> note.getGuid) ~
            ("title" -> note.getTitle) ~
            ("created" -> note.getCreated.toString) ~
            ("updated" -> note.getUpdated.toString)
          )
        )

    case GET(Path(Seg("note" :: guid :: Nil))) & AuthHeader(auth) =>
      val note = EvernoteService(auth).getNoteWithContent(guid)
      JsonContent ~>
        JsonResponse(
          ("guid" -> note.getGuid) ~
          ("title" -> note.getTitle) ~
          ("created" -> note.getCreated.toString) ~
          ("updated" -> note.getUpdated.toString) ~
          ("content" -> ENML.htmlize(note.getContent)) ~
          ResourcesElement(note)(auth)
        )

    case POST(Path(Seg("note" :: guid :: Nil))) & AuthHeader(auth) =>
      JsonContent ~>
        JsonResponse(
          ("guid" -> guid) ~
          ("title" -> "TODO") ~
          ("content" -> "TODO")
          // TODO
        )

    case POST(Path(Seg("note" :: guid :: "resource" :: Nil)) & MultiPart(req)) & AuthHeader(auth) =>
      MultiPartParams.Memory(req).files("file") match {
        case Seq(file, _*) =>
          val note = EvernoteService(auth).addResourceToNote(guid, file.name, file.contentType, file.bytes)
          JsonContent ~>
            JsonResponse(
              ("guid" -> note.getGuid) ~
              ResourcesElement(note)(auth)
            )

        case _ => BadRequest
      }

    case DELETE(Path(Seg("note" :: noteId :: "resource" :: resourceId :: Nil))) & AuthHeader(auth) =>
      val note = EvernoteService(auth).removeResource(noteId, resourceId)
      JsonContent ~>
        JsonResponse(
          ("guid" -> note.getGuid) ~
          ResourcesElement(note)(auth)
        )

    case GET(Path(Seg("resource" :: guid :: _ :: Nil))) & Params(ResourceKeyParam(key)) =>
      ResourceOneTimeKey.getOnce(key) match {
        case Some(ResourceOneTimeKey(guidInKey, auth, _)) if guidInKey == guid =>
          val resource = EvernoteService(auth).getResource(guid)
          ContentType(resource.getMime) ~>
            ContentLength(resource.getData.getSize.toString) ~>
            ResponseBytes(resource.getData.getBody)

        case _ => NotFound
      }
  }

  object ResourceKeyParam extends Params.Extract("o", Params.first ~> Params.nonempty)

  object ResourcesElement {
    import com.evernote.edam.`type`.Note

    def apply(note: Note)(auth: EvernoteAuth) =
      "resources" -> Option(note.getResources).map(_.map(resource =>
        ("guid" -> resource.getGuid) ~
        ("fileName" -> Option(resource.getAttributes).map(_.getFileName)) ~
        ("fileSize" -> Option(resource.getData).map(_.getSize)) ~
        ("mime" -> resource.getMime) ~
        ("key" -> ResourceOneTimeKey(resource.getGuid, auth).cache.key)))
  }

  object JsonResponse {
    import org.json4s.JsonAST.JValue

    def apply(json: JValue) = ResponseString(compact(render(json)))
  }

}
