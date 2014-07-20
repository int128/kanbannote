package services

import com.evernote.clients.ClientFactory
import com.evernote.edam.`type`._
import com.evernote.edam.notestore.NoteFilter
import com.evernote.edam.userstore.Constants._
import models.EvernoteAuth

import scala.collection.JavaConversions._

case class EvernoteService(factory: ClientFactory) {

  lazy val userStore = factory.createUserStoreClient()

  lazy val noteStore = factory.createNoteStoreClient()

  def checkVersion =
    userStore.checkVersion("Scala", EDAM_VERSION_MAJOR, EDAM_VERSION_MINOR)

  def findRecentNotes(offset: Int, count: Int) = {
    val filter = new NoteFilter()
    filter.setOrder(NoteSortOrder.CREATED.getValue)
    filter.setAscending(false)
    noteStore.findNotes(filter, offset, count).getNotes
  }

  def getNoteWithContent(guid: String) =
    noteStore.getNote(guid, true, false, false, false)

  def getResource(guid: String) =
    noteStore.getResource(guid, true, false, false, false)

  def addResourceToNote(guid: String, fileName: String, mime: String, bytes: Array[Byte]) = {
    val note = noteStore.getNote(guid, false, false, false, false)

    val data = new Data()
    data.setBody(bytes)
    val resourceAttributes = new ResourceAttributes()
    resourceAttributes.setFileName(fileName)
    val resource = new Resource()
    resource.setData(data)
    resource.setAttributes(resourceAttributes)
    resource.setMime(mime)

    val patch = new Note()
    patch.setGuid(note.getGuid)
    patch.setTitle(note.getTitle)
    patch.setResources(note.getResources)
    patch.addToResources(resource)

    noteStore.updateNote(patch)
  }

  def removeResource(noteId: String, resourceId: String) = {
    val note = noteStore.getNote(noteId, false, false, false, false)

    val patch = new Note()
    patch.setGuid(note.getGuid)
    patch.setTitle(note.getTitle)
    patch.setResources(note.getResources.filterNot(_.getGuid == resourceId))

    noteStore.updateNote(patch)
  }

}

object EvernoteService {

  def create(auth: EvernoteAuth) =
    EvernoteService(
      new ClientFactory(
        new com.evernote.auth.EvernoteAuth(auth.environment, auth.token)))

}
