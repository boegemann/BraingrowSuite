package org.braingrow.gritterweb

import org.braingrow.gritter.drools.twitterutil.IdentifiableText
import akka.actor.{ActorRef, Actor}
import net.liftweb.json.Serialization
import java.lang.Object
import org.braingrow.gritter.twitter.StatusEvent

/**
 * User: ibogemann
 * Date: 12/09/11
 * Time: 10:57
 */

class AdoptForPageActor(webSocketActor: ActorRef, historyManager: MessageHistoryManager[String])
  extends AbstractDublettenFilteringSortedListPageAdapter[IdentifiableText](webSocketActor,historyManager) {


  def createPageMessage(word: String, identifiables: scala.List[IdentifiableText], pos: String): Map[String, Any] = {
    Map("word" -> word, "count" -> identifiables.size, "statusEvent" ->
      identifiables.map(se =>
        Map(
          "id" -> se.statusEvent.idStr,
          "username" -> se.statusEvent.user.name,
          "userUrl" -> se.statusEvent.user.url,
          "text" -> se.statusEvent.text,
          "userImage" -> se.statusEvent.user.profileImageUrl,
          "screenName" -> se.statusEvent.user.screenName
        )

      ), "movement" -> pos)
  }
}