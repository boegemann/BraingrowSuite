package org.braingrow.gritter.rss.news

import org.scalatest.Spec
import java.util.Date
import org.scalatest.matchers.ShouldMatchers
import com.sun.syndication.feed.synd.{SyndContentImpl, SyndEntryImpl}
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

/**
 * User: ibogemann
 * Date: 23/09/11
 * Time: 16:12
 */
@RunWith(classOf[JUnitRunner])
class ContentGrabbingNewsDigesterTest extends Spec with ShouldMatchers {

  describe("A ContentGrabbingSyndEntryDigester") {
    describe("consumes SyndEntry objects") {
      it("forwards a fully filled in NewsItem object to the passed in 'forwardFunction' function") {
        var receivedItem: Option[NewsItem] = None
        val forwardFunction = (newsItem: NewsItem) => {
          receivedItem = Some(newsItem);
          println(newsItem.title)
        }
        val mockEntry = new SyndEntryImpl()
        mockEntry.setTitle("Mock Title")
        val desc = new SyndContentImpl()
        desc.setValue("Mock description")
        mockEntry.setDescription(desc)
        val dtePublished = new Date()
        mockEntry.setPublishedDate(dtePublished)
        mockEntry.setUpdatedDate(null)
        mockEntry.setLink("http://www.bbc.co.uk/news/world-europe-15045816")

        val digester = new ContentGrabbingSyndEntryDigester(forwardFunction, new BodyTextParser())
        digester.digest(mockEntry)

        Thread.sleep(5000)
        receivedItem should not be (None)
        val newsItem = receivedItem.get
        newsItem.title should equal("Mock Title")
        newsItem.description should equal("Mock description")
        newsItem.publishedDate should equal(dtePublished)
        newsItem.updatedDate should equal(None)
        newsItem.content.length() should be > 100
        newsItem.link should equal("http://www.bbc.co.uk/news/world-europe-15045816")
      }
    }
  }

}