package org.braingrow.gritter.rss.news

import akka.actor.Actor._
import org.braingrow.gritter.rss.RssListener
import com.sun.syndication.feed.synd.SyndEntry
import akka.camel.CamelServiceManager._
import collection.JavaConversions._

/**
 * User: ibogemann
 * Date: 24/09/11
 * Time: 09:02
 */

object NewsReaderApp {

  def main(args: Array[String]) {
    startCamelService
    val listener = actorOf(new RssListener("http://feeds.bbci.co.uk/news/rss.xml", (msg) => {
      val digester = new ContentGrabbingSyndEntryDigester((ni) => {
        println(ni.title)
        println(ni.content)
      }, new BbcFeedParser())
      msg.getEntries.foreach {
        entry => {
          digester.digest(entry.asInstanceOf[SyndEntry])
        }
      }
    }))
    getMandatoryService.awaitEndpointActivation(1) {
      listener.start()
    }
  }


}