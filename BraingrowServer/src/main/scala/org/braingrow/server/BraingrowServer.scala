package org.braingrow.server

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.handler.HandlerCollection
import akka.actor.Actor

/**
 * User: ibogemann
 * Date: 08/09/11
 * Time: 16:36
 */

class BraingrowServer(val port: Int) {


  private val server = new Server(port)
  private val handlers = new HandlerCollection(true)

  private def start() {

  }

  def startServerAsync() {
    Actor.spawn {
      startServer()
    }
  }

  def startServer() {
    server.setHandler(handlers)
    // Start the server and join it to avoid exit.
    server.start()
    server.join()
  }

  def addHandler(handler: Handler) {
    handlers.addHandler(handler)
  }

  def isStarted = server.isStarted

  def isStarting = server.isStarting


}