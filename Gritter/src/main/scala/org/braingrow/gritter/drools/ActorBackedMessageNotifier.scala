package org.braingrow.gritter.drools

import akka.actor.ActorRef

/**
 * User: ibogemann
 * Date: 08/09/11
 * Time: 13:14
 */

class ActorBackedMessageNotifier (actorRef:ActorRef) {
    def onMessage(message:Object) {
      actorRef ! message
    }
  }