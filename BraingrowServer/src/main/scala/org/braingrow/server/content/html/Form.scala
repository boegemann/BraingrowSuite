package org.braingrow.server.content.html

import org.braingrow.server.command.Command

/**
 * User: ibogemann
 * Date: 31/08/11
 * Time: 08:45
 */

case class Form (override val id: String = "",
                override val name:String = "",
               override val cssClass: String = "",
               val attributes: Map[String, String] = Map(),
               override val content: List[HtmlContent] = List(),
               override val events: Map[String, List[Command]] = Map())
  extends AbstractHtmlElement("form", id, name, cssClass, attributes, content, events) {

}