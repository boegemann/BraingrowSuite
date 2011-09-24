package org.braingrow.gritter.rss.news

/**
 * User: ibogemann
 * Date: 24/09/11
 * Time: 15:59
 */

class DefaultNewsAnalyser extends NewsAnalyser {
  def analyse(newsItem: NewsItem) = {
    val wordsOnly = newsItem.content.replaceAll("[^A-Za-z ]", " ").split(' ')
    val possibleWords = wordsOnly.map(s => {
      if (s.length() > 1 && s.toUpperCase == s) s else if (s.length > 3) s.toLowerCase else ""
    }).filter(_.length > 0)

    new AnalysedNewsItem(newsItem, wordsOnly.size, List())
  }
}
