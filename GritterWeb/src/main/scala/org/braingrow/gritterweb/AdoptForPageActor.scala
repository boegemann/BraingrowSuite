package org.braingrow.gritterweb

import org.braingrow.gritter.drools.twitterutil.IdentifiableText
import akka.actor.{ActorRef, Actor}
import net.liftweb.json.Serialization

/**
 * User: ibogemann
 * Date: 12/09/11
 * Time: 10:57
 */

class AdoptForPageActor(webSocketActor: ActorRef) extends Actor {
  implicit val formats = net.liftweb.json.DefaultFormats

  def receive = {
    case Tuple2(oldList: List[(String, List[IdentifiableText])], newList: List[(String, List[IdentifiableText])]) => {
      val orderedOldWords = oldList.map(_._1)
      webSocketActor ! Serialization.write(
        newList.view.zipWithIndex.map {
          case ((word, identifiables), index) => {
            val indexInOld = orderedOldWords.indexOf(word)

            val pos = indexInOld match {
              case idx if idx < 0 => "new"
              case idx: Int => idx.compare(index) match {
                case i if i < 0 => "down"
                case i if i > 0 => "up"
                case _ => "level"
              }
            }
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
      )
    }
  }


}

// case (acc, (value, index))