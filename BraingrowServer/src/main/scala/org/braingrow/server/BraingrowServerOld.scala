package org.braingrow.server

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.DefaultHandler
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.servlet.ServletHandler
import org.eclipse.jetty.util.resource.Resource
import org.braingrow.server.application.BraingrowApplication
import org.eclipse.jetty.servlet.ServletHolder

class BraingrowServerOld {
  def startServer(port: Int, applications: List[Tuple2[String, BraingrowApplication]]) {

    val server = new Server(port);

    // Create the servlet handler and define the Chat servlet
    val servletHandler = new ServletHandler()

    applications.foreach(tuple => {
      var holder = new ServletHolder();
      holder.setServlet(new ApplicationServlet(tuple._2));
      servletHandler.addServletWithMapping(holder, tuple._1)
    })

    // Create a resource handler for static content (eg index.html, chat.js, chat.css)
    val resourceHandler = new ResourceHandler()

    resourceHandler.setBaseResource(Resource.newClassPathResource("org/braingrow/staticcontent/"))

    // Create the default handler for all other requests
    val defaultHandler = new DefaultHandler()

    // Set the handlers on the server as a handler list
    val handlers = new HandlerList()
    handlers.setHandlers(Array(servletHandler, resourceHandler, defaultHandler))
    server.setHandler(handlers)

    // Start the server and join it to avoid exit.
    server.start();
    server.join();
  }
}