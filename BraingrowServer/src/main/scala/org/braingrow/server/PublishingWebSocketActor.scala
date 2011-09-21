package org.braingrow.server

import akka.actor.Actor
import org.eclipse.jetty.websocket.{WebSocket, WebSocketFactory}
import org.eclipse.jetty.websocket.WebSocket.Connection
import java.util.concurrent.CopyOnWriteArraySet
import scala.collection.JavaConversions._
import javax.servlet.http.HttpServletRequest
import org.eclipse.jetty.servlet.{ServletHolder, ServletHandler}

class PublishingWebSocketActor(server: BraingrowServer, path: String)
  extends AbstractBraingrowHandler(path) with Actor with WebSocketFactory.Acceptor {

  private val sockets = new CopyOnWriteArraySet[Socket]()
  start(server)

  protected def receive = {
    case s: String => {
      try {
        if (s.trim().equals("")) {
          println("Gotcha bugger!")
        }
        sockets.map{
          _.send(s)
        }
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }
  }

  def postOpen(socket: Socket) {

  }

  class Socket extends WebSocket {
    var lastmsg = "";

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
      postOpen(this)
    }

  }

  def checkOrigin(request: HttpServletRequest, origin: String) = true

  def doWebSocketConnect(request: HttpServletRequest, protocol: String) = {
    new Socket
  }

  protected def createHandler() = {
    val servletHandler = new ServletHandler()
    val holder = new ServletHolder();
    holder.setServlet(new WebSocketServlet(this));
    servletHandler.addServletWithMapping(holder, path)
    servletHandler
  }
}

