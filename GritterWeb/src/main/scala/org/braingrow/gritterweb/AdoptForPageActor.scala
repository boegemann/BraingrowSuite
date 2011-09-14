package org.braingrow.gritterweb

import org.braingrow.gritter.drools.twitterutil.IdentifiableText
import akka.actor.{ActorRef, Actor}
import net.liftweb.json.Serialization
import java.lang.Object

/**
 * User: ibogemann
 * Date: 12/09/11
 * Time: 10:57
 */

class AdoptForPageActor(webSocketActor: ActorRef, historyManager: ListHistoryManager) extends Actor {
  implicit val formats = net.liftweb.json.DefaultFormats

  private var oldList = List[(String, List[IdentifiableText])]()
  private var lock = new Object()

  def receive = {

    case newList: List[(String, List[IdentifiableText])] =>
      // synchronized as we need to avoid two same lists sneaking through

      val orderedList = lock.synchronized {
        if (newList == oldList) {
          None
        } else {
          val l = Some(oldList.map(_._1))
          oldList = newList
          l
        }
      }

      if (orderedList != None) {
        println({
          self.mailboxSize + ":" + newList.map(t => t._1 + ":" + t._2.size).mkString(",")
        })
        val orderedOldWords = orderedList.get
        val message = Serialization.write(
          Map(
            "dateTime" -> System.currentTimeMillis(),
            "list" -> newList.view.zipWithIndex.map {
              case ((word, identifiables), index) => {
                val indexInOld = orderedOldWords.indexOf(word)
                // determine whether the current item rose,fell or is new in top ten since the last list
                val pos = indexInOld match {
                  case idx if idx < 0 => "new"
                  case idx: Int => idx.compare(index) match {
                    case i if i < 0 => "down"
                    case i if i > 0 => "up"
                    case _ => "level"
                  }
                }
                // now create a Map containing the current word, all the information required from the
                // related status events, and its relative position so it can be serialised into the
                // json object for the client
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
            }.toList
          )
        )

        webSocketActor ! message
        historyManager.notifyList(message)

      }
  }
}