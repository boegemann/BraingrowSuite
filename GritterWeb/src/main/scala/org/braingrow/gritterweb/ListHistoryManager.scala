package org.braingrow.gritterweb

import collection.immutable.Queue
import java.lang.Object

/**
 * User: ibogemann
 * Date: 13/09/11
 * Time: 19:32
 */

class ListHistoryManager(historySize: Int) {
  // by initialising our list with an initial empty one we simplify our logic later ...
  private var history = Queue[String]();
  var lock = new Object()

  def notifyList(newMessage: String) {
    lock.synchronized {
      history = history.enqueue(newMessage)
      if (history.size > historySize) history = history.dequeue._2
    }
  }

  def getHistory = {
    history.toList
  }
}