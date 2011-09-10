package org.braingrow.server

import javax.servlet.http.HttpServlet
import org.braingrow.server.application.BraingrowApplication
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import org.eclipse.jetty.websocket.WebSocketFactory
class ApplicationServlet(val app: BraingrowApplication) extends HttpServlet {

  private val _wsFactory = new WebSocketFactory(new BraingrowWebsocketAcceptor(app))
  _wsFactory.setBufferSize(4096);
  _wsFactory.setMaxIdleTime(60000);

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {

    // If the WebSocket factory does not accept the connection, send the html instead:
    if (!_wsFactory.acceptWebSocket(req, resp)) {
      resp.getWriter().write(
        <html>
          <head>
            <title>
              {app.title}
            </title>
              <link type="text/css" href="css/ui-lightness/jquery-ui-1.8.16.custom.css" rel="Stylesheet"/>
            <script>
              var _token = '{app.token}';
            </script>
            <script type='text/javascript' src="js/jquery.js"></script>
            <script type='text/javascript' src="js/jquery-ui-1.8.16.custom.min.js"></script>
            <script type='text/javascript' src="js/braingrow.js"></script>
          </head>
          <body>
            <div id="app">
              {app.title}
              loading ...</div>
          </body>
        </html>.toString())
      resp.flushBuffer()
    }
  }

  override def init {

  }
}