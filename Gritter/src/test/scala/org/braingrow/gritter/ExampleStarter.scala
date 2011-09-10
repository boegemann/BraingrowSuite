package org.braingrow.gritter

import dispatch.:/
import akka.actor.Actor._
import twitter.TwitterStreamListener

/**
 * User: ibogemann
 * Date: 08/09/11
 * Time: 10:56
 */

object ExampleStarter {



  def main(args: Array[String]): Unit = {
    val s = :/("stream.twitter.com") / "1/statuses/sample.json" as ("IngoBoegemann", "arnsberg0855")
    val actorRef = actorOf[ExampleFlow].start()
    new TwitterStreamListener().startListener(s, actorRef)
  }
}