package org.braingrow.gritter.actors

import scala.collection.JavaConversions._
import akka.actor.{ActorRef, Actor}

/**
 * User: ibogemann
 * Date: 07/09/11
 * Time: 15:36
 */

class FilterSameListActor(val nextStep:ActorRef) extends Actor {

  private var lastList = List[Tuple2[String, Long]]()


  protected def receive = {
    case list: java.util.List[Tuple2[String, Long]] => {
      //println("FilterSameListActor: " + self.mailboxSize)
      val set = list.toSet
      val newList = set.groupBy {
        _._1
      }.map {
        _._2.head
      }.toList.sortWith((t1, t2) => (if (t1._2 == t2._2) t1._1 > t2._1 else t1._2 > t2._2)).slice(0, 10)
      if (!newList.equals(lastList)) {
        lastList = newList
        nextStep ! newList
      }
    }

  }
}