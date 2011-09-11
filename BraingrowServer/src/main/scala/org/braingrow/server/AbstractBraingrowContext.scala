package org.braingrow.server

import org.eclipse.jetty.server.handler.ContextHandler
import org.eclipse.jetty.server.Handler

/**
 * User: ibogemann
 * Date: 08/09/11
 * Time: 20:08
 */

abstract class AbstractBraingrowContext(path: String) {
  private var _started = false

  protected def createContext() = {
    val context = new ContextHandler();
    context.setContextPath(path);
    context.setResourceBase(".");
    context.setClassLoader(Thread.currentThread().getContextClassLoader);
    context
  }

  protected def createHandler(): Handler

  def start(server: BraingrowServer) {
    try {
      if (_started) throw new IllegalStateException("Context already started")
      _started = true
      val context = createContext()
      context.setHandler(createHandler())
      server.addHandler(context);

      // if the server is starting we wait for it
      while (server.isStarting) {
        Thread.sleep(100)
      }
      // if the server is already running we have to start the context ourselves
      if (server.isStarted) {
        context.start()
      }
    } catch {
      case e: Exception => e.printStackTrace();
    }
  }
}