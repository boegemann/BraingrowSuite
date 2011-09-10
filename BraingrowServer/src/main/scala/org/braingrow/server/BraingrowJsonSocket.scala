package org.braingrow.server

import org.eclipse.jetty.websocket.WebSocket
import org.eclipse.jetty.websocket.WebSocket.Connection
import org.braingrow.server.command.Command
import net.liftweb.json.JsonAST._
import net.liftweb.json.Extraction._
import net.liftweb.json.Printer._
class BraingrowJsonSocket(val acceptor: BraingrowWebsocketAcceptor) extends WebSocket.OnTextMessage {

  implicit val formats = net.liftweb.json.DefaultFormats
  @volatile
  private var _connection: Connection = _

  def sendCommands(cmds: List[Command]) {
    _connection.sendMessage(pretty(render(decompose(cmds.map(_.asMap)))))
  }

  def onOpen(connection: Connection) {
    _connection = connection;
    acceptor.addSocket(this)
  }

  /**
   * Callback for when a WebSocket connection is closed.
   * <p>Remove this WebSocket from the members set.
   */
  def onClose(closeCode: Int, message: String) {
    acceptor.removeSocket(this)
  }

  /**
   * Callback for when a WebSocket message is received.
   * <p>Send the message to all connections in the members set.
   */
  def onMessage(data: String) {
    println(data)
  }

}