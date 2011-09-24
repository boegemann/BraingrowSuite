package org.braingrow.gritterweb

import mockobjects.MockDublettenFilteringSortedListPageAdapater
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import akka.actor.Actor
import akka.actor.Actor._
import scala.util.parsing.json.JSON._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class AbstractDublettenFilteringSortedListPageAdapterTest extends Spec with ShouldMatchers {


  implicit val formats = net.liftweb.json.DefaultFormats

  describe("an AbstractDublettenFilteringSortedListPageAdapter") {
    describe("(when receiving Messages)") {

      val historyManager = new MessageHistoryManager[String](5)
      it("filters out sequential duplicates") {

        val wsActor = actorOf(new Actor {
          def receive = {
            case o: Object => {}
          }
        }).start()
        val adapter = actorOf(new MockDublettenFilteringSortedListPageAdapater(wsActor, historyManager)).start()

        def waitedForHistorySize = {
          Thread.sleep(300)
          historyManager.getHistory.size
        }

        try {
          adapter ! (List(("A", "A"), ("B", "A"), ("C", "A")))
          waitedForHistorySize should equal(1)

          adapter ! (List(("B", "B"), ("D", "A"), ("E", "A")))
          waitedForHistorySize should equal(2)


          adapter ! (List(("B", "B"), ("D", "A"), ("E", "A")))
          waitedForHistorySize should equal(2)

          adapter ! (List(("B", "B"), ("E", "A"), ("D", "A")))
          waitedForHistorySize should equal(3)


          adapter ! (List(("B", "B"), ("E", "A"), ("D", "A")))
          waitedForHistorySize should equal(3)
        } finally {
          adapter.stop()
        }
      }


      def geEntryItemPropertyAtPosition(entryNo: Int, prop: String, pos: Int) = {

        val parsedItems = historyManager.getHistory.map(parseFull(_))
        val entry = parsedItems(entryNo).get match {
          case map: Map[String, Object] => map
          case _ => sys.error("Unexpected parseResult")
        }
        entry
          .get("list")
          .get.asInstanceOf[List[Map[String, Object]]](pos)
          .get(prop)
          .get
      }

      it("in the oldest item all entries should be at position 'new'") {

        geEntryItemPropertyAtPosition(0, "word", 0) should equal("A")
        geEntryItemPropertyAtPosition(0, "pos", 0) should equal("new")

        geEntryItemPropertyAtPosition(0, "word", 1) should equal("B")
        geEntryItemPropertyAtPosition(0, "pos", 1) should equal("new")

        geEntryItemPropertyAtPosition(0, "word", 2) should equal("C")
        geEntryItemPropertyAtPosition(0, "pos", 2) should equal("new")


      }

      it("in the second item all entries should be at position 'new' apart from 'B' which should be 'up'") {

        geEntryItemPropertyAtPosition(1, "word", 0) should equal("B")
        geEntryItemPropertyAtPosition(1, "pos", 0) should equal("up")

        geEntryItemPropertyAtPosition(1, "word", 1) should equal("D")
        geEntryItemPropertyAtPosition(1, "pos", 1) should equal("new")

        geEntryItemPropertyAtPosition(1, "word", 2) should equal("E")
        geEntryItemPropertyAtPosition(1, "pos", 2) should equal("new")
      }

      it("in the fourth item the top item is 'level' ") {

        geEntryItemPropertyAtPosition(2, "word", 0) should equal("B")
        geEntryItemPropertyAtPosition(2, "pos", 0) should equal("level")

        geEntryItemPropertyAtPosition(2, "word", 1) should equal("E")
        geEntryItemPropertyAtPosition(2, "pos", 1) should equal("up")

        geEntryItemPropertyAtPosition(2, "word", 2) should equal("D")
        geEntryItemPropertyAtPosition(2, "pos", 2) should equal("down")
      }


    }
  }

}