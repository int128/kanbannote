package services

import com.evernote.auth._
import com.evernote.clients.ClientFactory
import com.evernote.edam.`type`.NoteSortOrder
import com.evernote.edam.notestore.NoteFilter
import com.evernote.edam.userstore.Constants._

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

}

object EvernoteService {

  def create(auth: EvernoteAuth) =
    EvernoteService(new ClientFactory(auth))

}
