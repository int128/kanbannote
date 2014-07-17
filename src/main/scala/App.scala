import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import services._
import unfiltered.request._
import unfiltered.response._

import scala.collection.JavaConversions._
import scala.util.Random

class App extends unfiltered.filter.Plan with Evernote with Memcache {

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
      val resourcesAccessKey = Random.alphanumeric.take(32).mkString
      putCache(resourcesAccessKey, auth)

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
            ("mime" -> resource.getMime)
          ))) ~
          ("resourcesAccessKey" -> resourcesAccessKey)
        )))

    case GET(Path(Seg("resource" :: guid :: Nil))) & Params(ResourcesAccessKey(key)) =>
      getCache[EvernoteAuth](key) match {
        case Some(auth) =>
          removeCache(key)

          val service = EvernoteService.create(auth)
          val resource = service.getResource(guid)

          ContentType(resource.getMime) ~>
            ContentLength(resource.getData.getSize.toString) ~>
            ResponseBytes(resource.getData.getBody)

        case _ => NotFound
      }
  }

  object ResourcesAccessKey extends Params.Extract("o", Params.first ~> Params.nonempty)

}
