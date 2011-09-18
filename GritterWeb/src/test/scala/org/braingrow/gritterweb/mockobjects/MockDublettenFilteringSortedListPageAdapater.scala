package org.braingrow.gritterweb.mockobjects

import akka.actor.Actor._
import org.braingrow.gritterweb.{MessageHistoryManager, AbstractDublettenFilteringSortedListPageAdapter}
import akka.actor.ActorRef

class MockDublettenFilteringSortedListPageAdapater(wsActorRef:ActorRef, historyManager:MessageHistoryManager[String])
  extends AbstractDublettenFilteringSortedListPageAdapter[String](wsActorRef,historyManager) {

  var callList = List[(String, String, String)]()

  override def postStop() {
    wsActorRef.stop()
  }

  def reset() {
    callList = List[(String, String, String)]()
  }

  def createPageMessage(word: String, list: String, pos: String) = {
    callList = callList.+:((word, list, pos))
    Map ("word"-> word, "list"->list, "pos" -> pos)
  }


}