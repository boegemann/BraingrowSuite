package org.braingrow.gritter.actors

import org.drools.runtime.StatefulKnowledgeSession
import akka.actor.Actor
/**
 * User: ibogemann
 * Date: 07/09/11
 * Time: 08:20
 */

class DroolsActor(knowledgeSession: StatefulKnowledgeSession) extends Actor {


  protected def receive = {
    case obj: Object => {
      //println("DroolsActor " : " + self.mailboxSize)
      knowledgeSession.insert(obj);
      knowledgeSession.fireAllRules();
    }
  }
}



