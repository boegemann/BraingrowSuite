package org.braingrow.gritter.drools.twitterutil

import org.braingrow.gritter.twitter.StatusEvent

/**
 * User: ibogemann
 * Date: 09/09/11
 * Time: 20:30
 */

object TextExtractor {
  protected val ignored = List("have", "into", "under",
    "with", "else", "what", "when", "where", "would", "never", "down", "left", "right",
    "that", "your", "out", "like", "know", "just", "this", "thanks",
    "they", "last", "udah", "love", "haha", "good", "from",
    "para", "cont", "http", "morning", "always", "will", "today", "take", "hahaha", "back", "twitter", "photo", "want",
    "time", "there", "still", "yang", "some", "about", "well", "think", "then", "going", "more", "make", "follow", "feel",
    "really", "only", "lagi", "sama", "please", "follow", "need", "first", "here", "even", "live", "hate", "great", "super", "fuck",
    "after", "even", "them", "much", "someone", "dont", "besok", "life", "come", "real", "mais", "best", "over", "tweet", "juga",
    "como", "kamu", "jadi", "bisa", "minha", "banget", "been", "hoje", "tapi", "kita", "kalo", "shit", "nada", "casa", "miss", "pero",
    "aqui", "than", "look", "tudo", "quem", "todos", "should", "agora", "quando", "porque", "their", "better", "check", "start"
  )

  def extractAllWords(statusEvent: StatusEvent, minChars: Int = 3): Traversable[String] = {
    statusEvent.text.replaceAll("[^A-Za-z ]", " ").trim().split(' ').filter(_.length() > minChars)
  }

  def extractAllWordsAsIdentifiableText(statusEvent: StatusEvent, minChars: Int = 3): Traversable[IdentifiableText] = {
    statusEvent.text.replaceAll("[^A-Za-z ]", " ")
      .split(' ')
      .map(_.toLowerCase().trim())
      .filter(_.length() > 3)
      .filter(!ignored.contains(_))
      .map(s => IdentifiableText(s, statusEvent.idStr))
  }

}