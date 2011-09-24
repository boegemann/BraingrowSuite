package org.braingrow.gritter.rss.news

/**
 * User: ibogemann
 * Date: 24/09/11
 * Time: 15:30
 */

case class AnalysedNewsItem(newsItem: NewsItem, wordCount: Int, countedWords: List[Tuple2[String, Int]])