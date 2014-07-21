
import models.EvernoteAuth
import unfiltered.request._

import scala.xml.Elem
import scala.xml.factory.XMLLoader

trait Evernote {

  object TokenHeader extends StringHeader("X-EN-Token")

  object EnvironmentHeader extends StringHeader("X-EN-Environment")

  object AuthHeader {
    def unapply[T](req: HttpRequest[T]) =
      (EnvironmentHeader(req), TokenHeader(req)) match {
        case (Some(environment), Some(token)) => EvernoteAuth(environment, token)
        case _ => None
      }
  }

  private object XML extends XMLLoader[Elem] {
    import javax.xml.parsers.{SAXParser, SAXParserFactory}

    override def parser: SAXParser = {
      val f = SAXParserFactory.newInstance()
      f.setValidating(false)
      f.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
      f.newSAXParser()
    }
  }

  object NoteContentResponse {
    def apply(xml: String): String = XML.loadString(xml).mkString
  }

  object NoteContentRequest {
    def apply(xml: String): String =
      """<!DOCTYPE en-note SYSTEM "http://xml.evernote.com/pub/enml2.dtd">""" +
        XML.loadString(xml).mkString
  }

}
