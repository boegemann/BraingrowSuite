package org.braingrow.gritter.actors

import org.braingrow.gritter.twitter.StatusEvent
import akka.actor.{ActorRef, Actor}

/**
 * User: ibogemann
 * Date: 06/09/11
 * Time: 20:01
 */

class SplitTextActor(val nextStep: ActorRef) extends Actor {
  val ignored = List("have", "into", "under",
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


  protected def receive = {
    case se: StatusEvent => {
      //println("SplitTextActor: " + self.mailboxSize)
      se.text.replaceAll("[^A-Za-z ]", " ").split(' ').map(_.toLowerCase.trim).filter(_.length() > 3).filter(!ignored.contains(_)).map(s => nextStep ! (s))
    }

  }
}