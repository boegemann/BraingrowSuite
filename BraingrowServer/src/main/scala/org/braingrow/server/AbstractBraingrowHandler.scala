package org.braingrow.server

import org.eclipse.jetty.server.Handler

/**
 * User: ibogemann
 * Date: 08/09/11
 * Time: 20:08
 */

abstract class AbstractBraingrowHandler(path: String) {
  private var _started = false

  protected def createHandler(): Handler

  def start(server: BraingrowServer) {
    try {
      if (_started) throw new IllegalStateException("Context already started")
      _started = true
      val handler = createHandler()
      server.addHandler(handler);
      // if the server is starting we wait for it
      while (server.isStarting) {
        Thread.sleep(100)
      }
      // if the server is already running we have to start the handler ourselves
      if (server.isStarted) {
        handler.start()
      }
    } catch {
      case e: Exception => e.printStackTrace();
    }
  }
}