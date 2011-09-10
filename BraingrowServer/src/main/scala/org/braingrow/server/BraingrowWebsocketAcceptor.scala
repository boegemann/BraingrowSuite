package org.braingrow.server

import org.eclipse.jetty.websocket.WebSocketFactory
import javax.servlet.http.HttpServletRequest
import java.util.concurrent.CopyOnWriteArraySet
import org.braingrow.server.application.BraingrowApplication

class BraingrowWebsocketAcceptor(app: BraingrowApplication) extends WebSocketFactory.Acceptor {

  private val members = new CopyOnWriteArraySet[BraingrowJsonSocket]()

  def checkOrigin(request: HttpServletRequest, origin: java.lang.String) = {
    // Allow all origins
    true;
  }

  def doWebSocketConnect(request: HttpServletRequest, protocol: String) = {
    // Return new ChatWebSocket for chat protocol connections
    if ("braingrow".equals(protocol)) {
      app.createJsonSocket(this)
    } else {
      null
    }
  }

  def addSocket(socket: BraingrowJsonSocket) {
    members.add(socket)
  }

  def removeSocket(socket: BraingrowJsonSocket) {
    members.remove(socket)
  }

}