import models._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import services._
import unfiltered.filter.request._
import unfiltered.request._
import unfiltered.response._

import scala.collection.JavaConversions._

class App extends unfiltered.filter.Plan with Evernote {

  def intent = {
    case GET(Path("/notes")) & AuthHeader(auth) =>
      val service = EvernoteService.create(auth)
      val notes = service.findRecentNotes(0, 100)

      JsonContent ~> ResponseString(compact(render(
        notes.map(note =>
          ("guid" -> note.getGuid) ~
          ("title" -> note.getTitle) ~
          ("created" -> note.getCreated.toString) ~
          ("updated" -> note.getUpdated.toString)
        )
      )))

    case GET(Path(Seg("note" :: guid :: Nil))) & AuthHeader(auth) =>
      val service = EvernoteService.create(auth)
      val note = service.getNoteWithContent(guid)

      JsonContent ~>
        ResponseString(compact(render(
          ("guid" -> note.getGuid) ~
          ("title" -> note.getTitle) ~
          ("created" -> note.getCreated.toString) ~
          ("updated" -> note.getUpdated.toString) ~
          ("content" -> note.getContent) ~
          ("resources" -> Option(note.getResources).map(_.map(resource =>
            ("guid" -> resource.getGuid) ~
            ("mime" -> resource.getMime) ~
            ("key" -> ResourceOneTimeKey(resource.getGuid, auth).cache.key)
          )))
        )))

    case POST(Path(Seg("note" :: guid :: "resource" :: Nil)) & MultiPart(req)) & AuthHeader(auth) =>
      MultiPartParams.Memory(req).files("file") match {
        case Seq(file, _*) =>
          val service = EvernoteService.create(auth)
          val note = service.addResourceToNote(guid, file.name, file.contentType, file.bytes)

          JsonContent ~>
            ResponseString(compact(render(
              ("guid" -> note.getGuid) ~
              ("resources" -> Option(note.getResources).map(_.map(resource =>
                ("guid" -> resource.getGuid) ~
                ("mime" -> resource.getMime) ~
                ("key" -> ResourceOneTimeKey(resource.getGuid, auth).cache.key)
              )))
            )))

        case _ => BadRequest
      }

    case GET(Path(Seg("resource" :: guid :: Nil))) & Params(ResourceKeyParam(key)) =>
      ResourceOneTimeKey.getOnce(key) match {
        case Some(ResourceOneTimeKey(guidInKey, auth, _)) if guidInKey == guid =>
          val service = EvernoteService.create(auth)
          val resource = service.getResource(guid)

          ContentType(resource.getMime) ~>
            ContentLength(resource.getData.getSize.toString) ~>
            ResponseBytes(resource.getData.getBody)

        case _ => NotFound
      }
  }

  object ResourceKeyParam extends Params.Extract("o", Params.first ~> Params.nonempty)

}
