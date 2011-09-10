package org.braingrow.gritter

import actors.{DroolsActor, NewListNotifier, FilterSameListActor, SplitTextActor}
import akka.actor.Actor
import akka.actor.Actor._
import drools.{KnowledgeSessionFactory, ActorBackedMessageNotifier}
import akka.routing.SmallestMailboxFirstIterator

import akka.routing.Routing._
import twitter.StatusEvent

/**
 * User: ibogemann
 * Date: 08/09/11
 * Time: 11:14
 */

class ExampleFlow extends Actor {

  private val notificationActor = actorOf[NewListNotifier].start()
  private val filterActor = actorOf(new FilterSameListActor(notificationActor)).start()

  private val droolsSession = KnowledgeSessionFactory.createKnowledgeSessionFromClassPathResource(
    "WordRules.drl", Map("listReceiver" -> new ActorBackedMessageNotifier(filterActor)))
  // the drools actor is the slowest - hence we use a load balancer to parallelize it
  val lba = loadBalancerActor(new SmallestMailboxFirstIterator(
    (1 to 4).map((i) => {
      actorOf(new DroolsActor(droolsSession)).start()
    })
  ))

  private val textSplitter = actorOf(new SplitTextActor(lba)).start();

  def receive = {
    case se: StatusEvent => {
      textSplitter ! se
    }

    case _ => {
      println("Unknown Message")
    }
  }
}