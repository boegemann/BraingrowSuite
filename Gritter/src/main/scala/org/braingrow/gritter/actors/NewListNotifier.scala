package org.braingrow.gritter.actors

import akka.actor.Actor

/**
 * User: ibogemann
 * Date: 07/09/11
 * Time: 15:43
 */

class NewListNotifier extends Actor {
  protected def receive = {

    case list: List[Tuple2[String, Long]] => {
      //println("NewListNotifier: " + self.mailboxSize)
      println(System.currentTimeMillis() + ": " + list);
    }
  }
}