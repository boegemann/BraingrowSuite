package org.braingrow.gritter.rss.news

/**
 * User: ibogemann
 * Date: 24/09/11
 * Time: 15:33
 */

trait NewsAnalyser {
  def analyse(newsItem: NewsItem): AnalysedNewsItem
}