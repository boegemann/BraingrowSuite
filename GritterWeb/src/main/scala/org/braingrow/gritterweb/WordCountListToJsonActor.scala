package org.braingrow.gritterweb

import net.liftweb.json.Serialization
import akka.actor.{ActorRef, Actor}
import java.lang.Object

/**
 * User: ibogemann
 * Date: 09/09/11
 * Time: 09:00
 */

class WordCountListToJsonActor(socketActor: ActorRef) extends Actor {
  implicit val formats = net.liftweb.json.DefaultFormats

  private var curList = List[Map[String, Long]]()
  private val lock = new Object()


  protected def receive = {
    case list: List[Tuple2[String, Long]] => {
      //println("NewListNotifier: " + self.mailboxSize)
      println(System.currentTimeMillis() + ": " + list);

      lock.synchronized {
        val newMap = list.toMap;
        // remove all keys from the historic maps that not present in the new map
        curList.map(m => {
          m.filterKeys(newMap.contains(_))
        })
        // prepend to current map and drop the oldest
        curList = (curList.+:(list.toMap)).slice(0,10)
      }


      socketActor ! Serialization.write(curList)
    }
  }
}