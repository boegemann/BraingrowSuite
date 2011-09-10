package org.braingrow.server.content.html

import org.braingrow.server.command.Command

case class Div(override val id: String = "",
                override val name:String = "",
               override val cssClass: String = "",
               val attributes: Map[String, String] = Map(),
               override val content: List[HtmlContent] = List(),
               override val events: Map[String, List[Command]] = Map())
  extends AbstractHtmlElement("div", id, name, cssClass, attributes, content, events) {

}