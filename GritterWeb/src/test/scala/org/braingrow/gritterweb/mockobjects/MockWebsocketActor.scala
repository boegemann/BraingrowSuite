package org.braingrow.gritterweb.mockobjects

import akka.actor.Actor

class MockWebsocketActor extends Actor {
    var messages = List[Object]()

    protected def receive = {
      case o: Object => messages = messages.+:(o)
    }
  }