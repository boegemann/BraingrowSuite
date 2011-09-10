package org.braingrow.gritterweb

import akka.actor.Actor
import org.braingrow.server.{PublishingWebSocketActor, StaticClasspathContentRoot, BraingrowServer}
import org.braingrow.gritter.twitter.TwitterStreamListener
import dispatch.:/
import org.braingrow.gritter.drools.{ActorBackedMessageNotifier, KnowledgeSessionFactory}
import akka.routing.Routing._
import akka.routing.SmallestMailboxFirstIterator
import akka.actor.Actor._
import org.braingrow.gritter.actors.{SplitTextActor, DroolsActor, FilterSameListActor}

/**
 * User: ibogemann
 * Date: 08/09/11
 * Time: 16:53
 */

object GritterWeb {

  val s = :/("stream.twitter.com") / "1/statuses/sample.json" as ("IngoBoegemann", "arnsberg0855")


  def main(args: Array[String]): Unit = {
    val server = new BraingrowServer(8080);
    Actor.spawn {
      server.startServer();
    }
    new StaticClasspathContentRoot("/gritter", "org/braingrow/gritterweb/static").start(server)

    // Actor flow configuration
    val webSocketActor = actorOf(new PublishingWebSocketActor(server, "/gritter/ws")).start()
    val countToJsonActor = actorOf(new WordCountListToJsonActor(webSocketActor)).start()
    val filterActor = actorOf(new FilterSameListActor(countToJsonActor)).start()
    val droolsSession = KnowledgeSessionFactory.createKnowledgeSessionFromClassPathResource(
      "WordRules.drl", Map("listReceiver" -> new ActorBackedMessageNotifier(filterActor)))
    // the drools actor is the slowest - hence we use a load balancer to parallelize it
    val lba = loadBalancerActor(new SmallestMailboxFirstIterator(
      (1 to 2).map((i) => {
        actorOf(new DroolsActor(droolsSession)).start()
      })
    ))

    val textSplitter = actorOf(new SplitTextActor(lba)).start();

    new TwitterStreamListener().startListener(s, textSplitter)

  }
}