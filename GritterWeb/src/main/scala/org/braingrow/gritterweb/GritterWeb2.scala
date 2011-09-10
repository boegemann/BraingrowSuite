package org.braingrow.gritterweb

import dispatch.:/
import akka.actor.Actor
import scala.collection.JavaConversions._
import akka.actor.Actor._
import org.braingrow.server.{StaticClasspathContentRoot, BraingrowServer, PublishingWebSocketActor}
import org.braingrow.gritter.twitter.{StatusEvent, TwitterStreamListener}
import org.braingrow.gritter.drools.{ActorBackedMessageNotifier, KnowledgeSessionFactory}
import net.liftweb.json.Serialization
import org.braingrow.gritter.drools.twitterutil.{DynamicSizeRegulator, TextExtractor, IdentifiableText}

/**
 * User: ibogemann
 * Date: 08/09/11
 * Time: 16:53
 */

object GritterWeb2 {

  implicit val formats = net.liftweb.json.DefaultFormats

  val s = :/("stream.twitter.com") / "1/statuses/sample.json" as("IngoBoegemann", "arnsberg0855")


  def main(args: Array[String]): Unit = {
    val server = new BraingrowServer(8080);
    Actor.spawn {
      server.startServer();
    }
    new StaticClasspathContentRoot("/gritter", "org/braingrow/gritterweb/static").start(server)

    val webSocketActor = actorOf(new PublishingWebSocketActor(server, "/gritter/ws")).start()
    val regulator = new DynamicSizeRegulator()
    val droolsSession = KnowledgeSessionFactory.createKnowledgeSessionFromClassPathResource(
      "StatusEventRules.drl", Map(
        "sizeRegulator" -> regulator,
        "notifier" -> new ActorBackedMessageNotifier(actorOf(new Actor {
          def receive = {
            case list: java.util.List[Tuple3[String, Int, IdentifiableText]] => {
              val safeList = list.synchronized {
                list.toSet.toList
              }
              webSocketActor ! Serialization.write(safeList.sortWith((t1, t2) => {
                if (t1._2 == t2._2) t1._1 > t2._1 else t1._2 > t2._2
              }))
              println(safeList)
            }
          }
        }).start())))


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