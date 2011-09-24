package org.braingrow.gritter.rss

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import akka.actor.Actor._
import akka.actor.ActorInitializationException
import akka.camel.CamelServiceManager._
import collection.JavaConversions._
import com.sun.syndication.feed.synd.SyndEntry
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class RssListenerTest extends Spec with ShouldMatchers {

  describe("An Rss Listener") {
    val service = startCamelService
    var ct = 0
    it("should throw an exception if no feed is passed in") {
      evaluating {
        new RssListener(null, (msg) => {})
      } should produce[ActorInitializationException]
    }
    it("should throw an exception if no digester function is passed in") {
      evaluating {
        new RssListener("http://feeds.bbci.co.uk/news/rss.xml", null)
      } should produce[ActorInitializationException]
    }

    it("should forward RSS feeds received one by one to the passed in Digester function") {
      val listener = actorOf(new RssListener("http://feeds.bbci.co.uk/news/rss.xml", (msg) => {
        ct += 1
        msg.getEntries.foreach(e => {
          val entry = e.asInstanceOf[SyndEntry]
          println(entry.getPublishedDate + ": " + entry.getUpdatedDate)
          println(entry.getTitle)
          println(entry.getDescription.getValue)
          println(entry.getLink)
        })
      }))
      getMandatoryService.awaitEndpointActivation(1) {
        listener.start()
      }
      Thread.sleep(2000)
      ct should be > 0
      stopCamelService
      akka.actor.Actors.registry().shutdownAll()
    }
  }
}