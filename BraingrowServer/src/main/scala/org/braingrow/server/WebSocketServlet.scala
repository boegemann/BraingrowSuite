package org.braingrow.server

import org.eclipse.jetty.websocket.WebSocketFactory
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}

/**
 * User: ibogemann
 * Date: 08/09/11
 * Time: 18:40
 */

class WebSocketServlet (acceptor: WebSocketFactory.Acceptor) extends HttpServlet {
  private val _wsFactory = new WebSocketFactory(acceptor)
  _wsFactory.setBufferSize(4096);
  _wsFactory.setMaxIdleTime(60000);

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    _wsFactory.acceptWebSocket(req, resp)
  }
}