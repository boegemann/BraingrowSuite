package org.braingrow.gritterweb

import org.braingrow.gritter.drools.twitterutil.IdentifiableText
import akka.actor.ActorRef

class AdoptTimezonelistForPageActor(webSocketActor: ActorRef, historyManager: MessageHistoryManager[String])
  extends AbstractDublettenFilteringSortedListPageAdapter[Int](webSocketActor,historyManager) {


  def createPageMessage(word: String, count: Int, pos: String): Map[String, Any] = {
    Map("word" -> word, "count" -> count)
  }
}