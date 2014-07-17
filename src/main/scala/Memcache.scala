import java.util.logging.Level

import com.google.appengine.api.memcache._

trait Memcache {
  private lazy val memcacheService = {
    val s = MemcacheServiceFactory.getMemcacheService
    s.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO))
    s
  }

  def getCache[T](key: String): Option[T] =
    Option(memcacheService.get(key).asInstanceOf[T])

  def putCache[T](key: String, value: T): T = {
    memcacheService.put(key, value)
    value
  }

  /**
   * Get the cached entry or calculate the value.
   *
   * @param key cache key
   * @param value cache value, evaluated only if cache does not found
   * @tparam T type of the value
   * @return cached entry or calculated value
   */
  def getCacheOrValue[T](key: String, value: => T): T =
    getCache(key) match {
      case Some(cached) => cached
      case None => putCache(key, value)
    }
}
