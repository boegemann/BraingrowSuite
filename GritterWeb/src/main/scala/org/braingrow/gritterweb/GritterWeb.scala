package org.braingrow.gritterweb

import dispatch.:/
import akka.actor.Actor
import akka.actor.Actor._
import org.braingrow.server.{StaticClasspathContentRoot, BraingrowServer, PublishingWebSocketActor}
import org.braingrow.gritter.twitter.{StatusEvent, TwitterStreamListener}
import org.braingrow.gritter.drools.{ActorBackedMessageNotifier, KnowledgeSessionFactory}
import org.braingrow.gritter.drools.twitterutil.{TextExtractor, IdentifiableText}
import net.liftweb.json.Serialization

/**
 * User: ibogemann
 * Date: 08/09/11
 * Time: 16:53
 */

object GritterWeb {

  implicit val formats = net.liftweb.json.DefaultFormats


  def main(args: Array[String]) {
    val server = new BraingrowServer(8080);
    if (args.size != 2) {
      println("You need to start the application with a username and password for your Twitter account!")
    } else {
      val s = :/("stream.twitter.com") / "1/statuses/sample.json" as(args(0), args(1))
      Actor.spawn {
        server.startServer();
      }
      new StaticClasspathContentRoot("/gritter", "org/braingrow/gritterweb/static").start(server)

      val webSocketActor = actorOf(new PublishingWebSocketActor(server, "/gritter/ws")).start()
      val droolsSession = KnowledgeSessionFactory.createKnowledgeSessionFromClassPathResource(
        "StatusEventRules.drl", Map(
          "notifier" -> new ActorBackedMessageNotifier(actorOf(new Actor {
            var lastList = List[(String, List[IdentifiableText])]()

            def receive = {
              case list: List[(String, List[IdentifiableText])] =>
                if (list != lastList) {
                  println({
                    self.mailboxSize + ":" + list.map(t => t._1 + ":" + t._2.size).mkString(",")
                  })
                  lastList = list
                  webSocketActor ! Serialization.write(list.map(
                    (t) => {
                      Map(t._1 -> Map("count" -> t._2.size, "ids" -> t._2.map(_.statusId)))
                    }
                  ))
                }


            }
          }
          ).start())))


      new TwitterStreamListener().startListener(s, actorOf(new Actor {
        def receive = {
          case s: StatusEvent =>
            TextExtractor.insertAllWordsAsIdentifiableAndFireRules(
              droolsSession,
              s,
              3
            )
        }
      }).start())
    }


  }
}