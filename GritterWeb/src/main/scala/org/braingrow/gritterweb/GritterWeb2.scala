package org.braingrow.gritterweb

import org.braingrow.gritter.twitter.TwitterStreamListener
import dispatch.:/
import org.braingrow.gritter.drools.KnowledgeSessionFactory
import akka.actor.Actor._
import org.braingrow.gritter.actors.DroolsActor

/**
 * User: ibogemann
 * Date: 08/09/11
 * Time: 16:53
 */

object GritterWeb2 {

  val s = :/("stream.twitter.com") / "1/statuses/sample.json" as("IngoBoegemann", "arnsberg0855")


  def main(args: Array[String]): Unit = {
    //    val server = new BraingrowServer(8080);
    //    Actor.spawn {
    //      server.startServer();
    //    }
    //    new StaticClasspathContentRoot("/gritter", "org/braingrow/gritterweb/static").start(server)


    val droolsSession = KnowledgeSessionFactory.createKnowledgeSessionFromClassPathResource(
      "StatusEventRules.drl", Map())
    // the drools actor is the slowest - hence we use a load balancer to parallelize it

    val droolsActor = actorOf(new DroolsActor(droolsSession)).start()



    new TwitterStreamListener().startListener(s, droolsActor)

  }
}