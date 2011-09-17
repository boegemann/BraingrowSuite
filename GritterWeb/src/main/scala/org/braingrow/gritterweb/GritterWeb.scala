package org.braingrow.gritterweb

import dispatch.:/
import akka.actor.Actor
import akka.actor.Actor._
import org.braingrow.server.{StaticClasspathContentRoot, BraingrowServer, PublishingWebSocketActor}
import org.braingrow.gritter.twitter.{StatusEvent, TwitterStreamListener}
import org.braingrow.gritter.drools.{ActorBackedMessageNotifier, KnowledgeSessionFactory}
import org.braingrow.gritter.drools.twitterutil.TextExtractor
import java.lang.Object

/**
 * User: ibogemann
 * Date: 08/09/11
 * Time: 16:53
 */
object GritterWeb {

  implicit val formats = net.liftweb.json.DefaultFormats

  def main(args: Array[String]) {
    val server = new BraingrowServer(8080);
    val lock = new Object
    if (args.size != 2) {
      println("You need to start the application with a username and password for your Twitter account!")
    } else {
      val s = :/("stream.twitter.com") / "1/statuses/sample.json" as(args(0), args(1))

      // add our various contexts to the server
      new StaticClasspathContentRoot("/gritter", "org/braingrow/gritterweb/static").start(server)


      val historyManager = new MessageHistoryManager[String](10);
      val webSocketActor = actorOf(new PublishingWebSocketActor(server, "/gritter/ws") {
        override def postOpen(socket: this.type#Socket) {
          historyManager.getHistory.foreach(
            socket.send(_)
          )
        }
      }).start()

      // The 'ActorBackedMessageNotifier' is an utility class to allow us to send messages
      // to an actor from within a Drools ruleset
      // In this case we add our AdoptWordlistForPageActor as an ActorRef in - Purpose of this actor is to take the
      // notification from drools and filter and 'massage' them to the correct json expected by the javascript in our page
      val WordNotifier = new ActorBackedMessageNotifier(actorOf(new AdoptWordlistForPageActor(webSocketActor, historyManager)).start())
      val TimeZoneNotifier = new ActorBackedMessageNotifier(actorOf(new Actor {
        protected def receive = {
          case l: List[(String, Int)] => println(l)
        }
      }).start())

      // We can now create our drools session and pass the notifier in
      val droolsWordAnalysisSession = KnowledgeSessionFactory.createKnowledgeSessionFromClassPathResource(
        "StatusEventRules.drl", Map(
          "notifier" -> WordNotifier
        )
      )

      val droolsTimezoneAnalysisSession = KnowledgeSessionFactory.createKnowledgeSessionFromClassPathResource(
        "TimezoneRules.drl", Map(
          "notifier" -> TimeZoneNotifier
        )
      )

      // All that's left to do is for us to create and start our Twitter Stream listener,
      // extract the 'IdentifiableText' items from the Status events received and
      // place them into the drools session
      new TwitterStreamListener().startListener(s, actorOf(new Actor {
        def receive = {
          case s: StatusEvent =>
            TextExtractor.insertAllWordsAsIdentifiableAndFireRules(droolsWordAnalysisSession, s, 3)
            TextExtractor.insertTimezoneAndFireRules(droolsTimezoneAnalysisSession, s, 3)
        }
      }).start())
    }


    //start an embedded Server in it's own Thread
    server.startServer();
  }
}