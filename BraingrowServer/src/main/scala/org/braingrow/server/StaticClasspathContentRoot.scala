package org.braingrow.server

import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.util.resource.Resource

/**
 * User: ibogemann
 * Date: 08/09/11
 * Time: 19:41
 */

class StaticClasspathContentRoot(path: String, classpath: String) extends AbstractBraingrowContext(path: String) {

  protected def createHandler() = {
    val resourceHandler = new ResourceHandler()
    resourceHandler.setBaseResource(Resource.newClassPathResource(classpath))
    resourceHandler
  }

}