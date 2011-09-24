package org.braingrow.gritter.rss.news

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import java.util.Date
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class DefaultNewsAnalyserTest extends Spec with ShouldMatchers {
  def newMockNewsItem1(): NewsItem = {
    new NewsItem(
      new Date(),
      None,
      "Mock News Item",
      "Mock News Item Description",
      "http//www.somewhere.com",
      "Once upon, a sodding night,5 Soldiers from the UN arrived"
    )
  }

  describe("A DefaultNewsAnalyser") {
    it("consumes NewsItem objects and produces AnalysedNewsItem objects") {
      val newsItem = newMockNewsItem1()
      val analysedNewsItem = new DefaultNewsAnalyser().analyse(newsItem)
      analysedNewsItem should not be null
    }
  }
}