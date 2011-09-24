package org.braingrow.gritter.rss

import akka.actor.Actor
import akka.camel.{Message, Consumer}
import com.sun.syndication.feed.synd.SyndFeed
import scala.Predef._

/**
 * User: ibogemann
 * Date: 22/09/11
 * Time: 19:24
 */

class RssListener(val feedUrl: String, val digester: (SyndFeed => Unit)) extends Actor with Consumer {

  require(feedUrl != null, "feedUrl can't be null")
  require(digester != null, "digester must be provided")


  def endpointUri = "rss://" + feedUrl


  protected def receive = {
    case s: Message => {
      digester(s.getBodyAs(classOf[SyndFeed]))
    }
  }
}