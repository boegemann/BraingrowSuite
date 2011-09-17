package org.braingrow.gritterweb

import collection.immutable.Queue
import java.lang.Object

/**
 * User: ibogemann
 * Date: 13/09/11
 * Time: 19:32
 */

class MessageHistoryManager[T](historySize: Int) {
  // by initialising our list with an initial empty one we simplify our logic later ...
  private var history = Queue[T]();
  var lock = new Object()

  def notifyMessage(newMessage: T) {
    lock.synchronized {
      history = history.enqueue(newMessage)
      if (history.size > historySize) history = history.dequeue._2
    }
  }

  def getHistory = {
    history.toList
  }
}