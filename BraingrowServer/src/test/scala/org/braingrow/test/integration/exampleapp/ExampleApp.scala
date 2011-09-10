package org.braingrow.test.integration.exampleapp

import org.braingrow.server.application.BraingrowApplication
import org.braingrow.server.BraingrowServerOld
import org.braingrow.server.BraingrowJsonSocket
import org.braingrow.server.BraingrowWebsocketAcceptor
import org.eclipse.jetty.websocket.WebSocket.Connection
import org.braingrow.server.content.html._

import org.braingrow.server.content.html.input._
import org.braingrow.server.command.{SetData, PlaceContent, ShowDialog}

object ExampleApp {
  def main(args: Array[String]): Unit = {

    class ExampleApp extends BraingrowApplication {
      val title = "Example Application"

      def createJsonSocket(acceptor: BraingrowWebsocketAcceptor): BraingrowJsonSocket = {
        new ExampleJsonSocket(acceptor);
      }
    }

    class ExampleJsonSocket(override val acceptor: BraingrowWebsocketAcceptor) extends BraingrowJsonSocket(acceptor) {
      override def onOpen(connection: Connection) {
        super.onOpen(connection)
        val form = Form(name = "form1", content = List(TextContent("First Name:"), Textbox(name = "firstName"), Button(caption = "Click Me")))
        val divDialog = Div(
          id = "id2",
          content = List(form)
        )
        val div = Div(id = "id1", cssClass = "css", content = List(TextContent("Frieda")), events = Map("click" -> List(ShowDialog("A Dialog", divDialog))))

        super.sendCommands(List(PlaceContent(div, "#app"), SetData(slot = "customer", data = Map("firstName" -> "Ingo", "lastName" -> "Boegemann"))))
      }
    }

    new BraingrowServerOld().startServer(8080, List(("/example", new ExampleApp)))
  }
}
