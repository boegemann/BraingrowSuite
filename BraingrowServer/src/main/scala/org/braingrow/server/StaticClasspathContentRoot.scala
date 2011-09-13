package org.braingrow.server

import org.eclipse.jetty.util.resource.Resource
import org.eclipse.jetty.server.handler.{ContextHandler, ResourceHandler}

/**
 * User: ibogemann
 * Date: 08/09/11
 * Time: 19:41
 */

class StaticClasspathContentRoot(path: String, classpath: String) extends AbstractBraingrowHandler(path: String) {

  protected def createHandler() = {
    val resourceHandler = new ResourceHandler()
    resourceHandler.setBaseResource(Resource.newClassPathResource(classpath))
    resourceHandler

    val context = new ContextHandler();
    context.setContextPath(path);
    context.setResourceBase(".");
    context.setClassLoader(Thread.currentThread().getContextClassLoader);
    context.setHandler(resourceHandler);
    context
  }

}