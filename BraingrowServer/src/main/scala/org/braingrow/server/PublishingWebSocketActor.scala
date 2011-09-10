package org.braingrow.server

import akka.actor.Actor
import org.eclipse.jetty.websocket.{WebSocket, WebSocketFactory}
import org.eclipse.jetty.websocket.WebSocket.Connection
import java.util.concurrent.CopyOnWriteArraySet
import scala.collection.JavaConversions._
import javax.servlet.http.HttpServletRequest
import org.eclipse.jetty.servlet.{ServletHolder, ServletHandler}

class PublishingWebSocketActor(server: BraingrowServer, path: String) extends Actor with WebSocketFactory.Acceptor {

  private var _started = false
  private val sockets = new CopyOnWriteArraySet[Socket]()
  start(server)

  protected def receive = {
    case s: String => {
      try {
        sockets.map(_.send(s))
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }
  }

  class Socket extends WebSocket {
    @volatile
    private var _connection: Connection = _

    def send(s: String) {
      _connection.sendMessage(s)
    }

    def onClose(closeCode: Int, message: String) {
      sockets.remove(this)
    }

    def onOpen(connection: Connection) {
      _connection = connection
      sockets.add(this)
    }
  }

  def checkOrigin(request: HttpServletRequest, origin: String) = true

  def doWebSocketConnect(request: HttpServletRequest, protocol: String) = {
    new Socket
  }

  def start(server: BraingrowServer) {
    if (_started) throw new IllegalStateException("Context already started")
    _started = true

    val servletHandler = new ServletHandler()
    val holder = new ServletHolder();
    holder.setServlet(new WebSocketServlet(this));
    servletHandler.addServletWithMapping(holder, path)

    server.addHandler(servletHandler);

    // if the server is starting we wait for it
    while (server.isStarting) {
      Thread.sleep(100)
    }
    // if the server is already running we have to start the context ourselves
    if (server.isStarted) {
      servletHandler.start()
    }
  }
}

