package org.braingrow.gritterweb

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{Spec, FeatureSpec}

class MessageHistoryManagerTest extends Spec with ShouldMatchers {

  case class SimpleObject(str: String, int: Int)

  describe("A MessageHistoryManager") {
    val intMHM = new MessageHistoryManager[Int](10)
    val stringMHM = new MessageHistoryManager[String](10)
    val objectMHM = new MessageHistoryManager[SimpleObject](5)

    describe("(when new)") {
      it("should be empty") {
        intMHM.getHistory.size should equal(0)
        stringMHM.getHistory.size should equal(0)
        objectMHM.getHistory.size should equal(0)
      }
    }

    describe("(when itmems are added)") {
      it("before the maximum number is reached the historysize should match the amount of items added") {
        intMHM.notifyMessage(1)
        stringMHM.notifyMessage("A")
        objectMHM.notifyMessage(SimpleObject("A", 1))

        intMHM.getHistory.size should equal(1)
        stringMHM.getHistory.size should equal(1)
        objectMHM.getHistory.size should equal(1)

        intMHM.notifyMessage(2)
        stringMHM.notifyMessage("B")
        objectMHM.notifyMessage(SimpleObject("B", 2))
        intMHM.notifyMessage(3)
        stringMHM.notifyMessage("C")
        objectMHM.notifyMessage(SimpleObject("C", 3))

        intMHM.getHistory.size should equal(3)
        stringMHM.getHistory.size should equal(3)
        objectMHM.getHistory.size should equal(3)

      }

      it("can contain the maximum of items allowed") {
        objectMHM.notifyMessage(SimpleObject("D", 4))
        objectMHM.notifyMessage(SimpleObject("E", 5))
        objectMHM.getHistory.size should equal(5)
      }

      it("the history method will keep the order in which items are added") {
        objectMHM.getHistory(0) should equal(SimpleObject("A", 1))
        objectMHM.getHistory(2) should equal(SimpleObject("C", 3))
        objectMHM.getHistory(4) should equal(SimpleObject("E", 5))
      }

      it("can't contain more than the number of maximum items allowed") {
        objectMHM.notifyMessage(SimpleObject("F", 6))
        objectMHM.getHistory.size should equal(5)
      }

      it("it will return the last max number of items") {
        objectMHM.getHistory(0) should equal(SimpleObject("B", 2))
        objectMHM.getHistory(2) should equal(SimpleObject("D", 4))
        objectMHM.getHistory(4) should equal(SimpleObject("F", 6))
      }
    }
  }
}