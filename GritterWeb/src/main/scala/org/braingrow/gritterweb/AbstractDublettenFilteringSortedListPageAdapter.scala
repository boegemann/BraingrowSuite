package org.braingrow.gritterweb

import org.braingrow.gritter.drools.twitterutil.IdentifiableText
import net.liftweb.json.Serialization
import akka.actor.{ActorRef, Actor}


abstract class AbstractDublettenFilteringSortedListPageAdapter[T](webSocketActor: ActorRef, historyManager: MessageHistoryManager[String])
  extends Actor {

  implicit val formats = net.liftweb.json.DefaultFormats

  private var oldList = List[(String, T)]()
  private var lock = new Object()

  def createPageMessage(word: String, list: T, pos: String): Map[String, Any]

  def createMessage(newList: scala.List[(String, T)], orderedOldWords: List[String]): Map[String, Any] = {

    val result = Map(
      "dateTime" -> System.currentTimeMillis(),
      "list" -> newList.view.zipWithIndex.map {
        case ((item, list), index) => {
          val indexInOld = orderedOldWords.indexOf(item)
          // determine whether the current item rose,fell or is new in top ten since the last list
          val pos = indexInOld match {
            case idx if idx < 0 => "new"
            case idx: Int => idx.compare(index) match {
              case i if i < 0 => "down"
              case i if i > 0 => "up"
              case _ => "level"
            }
          }
          createPageMessage(item, list, pos)

        }
      }.toList
    )
    result
  }

  def receive = {

    case newList: List[(String, T)] => {
      // synchronized as we need to avoid two same lists sneaking through

      val listOption = lock.synchronized {
        if (newList == oldList) {
          None
        } else {
          val l = Some(oldList.map(_._1))
          oldList = newList
          l
        }
      }

      if (listOption != None) {
        val orderedOldWords = listOption.get
        val result: Map[String, Any] = createMessage(newList, orderedOldWords)

        val message = Serialization.write(result)
        historyManager.notifyMessage(message)
        webSocketActor ! message

      }
    }
  }
}