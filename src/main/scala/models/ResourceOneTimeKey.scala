package models

import services.Memcache

import scala.util.Random

case class ResourceOneTimeKey(guid: String,
                              auth: EvernoteAuth,
                              key: String = Random.alphanumeric.take(64).mkString) {

  def cache: ResourceOneTimeKey = {
    Memcache.put(key, this)
    this
  }

}

object ResourceOneTimeKey {

  def get(key: String): Option[ResourceOneTimeKey] = Memcache.get(key)

  def getOnce(key: String): Option[ResourceOneTimeKey] =
    get(key) map { value =>
      Memcache.delete(key)
      value
    }

}
