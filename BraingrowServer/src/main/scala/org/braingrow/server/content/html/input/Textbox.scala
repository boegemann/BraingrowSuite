package org.braingrow.server.content.html.input

import org.braingrow.server.command.Command
import org.braingrow.server.content.html.{AbstractHtmlElement, HtmlContent}

/**
 * User: ibogemann
 * Date: 31/08/11
 * Time: 08:07
 */

case class Textbox (override val id: String = "",
                override val name:String = "",
               override val cssClass: String = "",
               val attributes: Map[String, String] = Map(),
               override val content: List[HtmlContent] = List(),
               override val events: Map[String, List[Command]] = Map())
  extends AbstractHtmlElement("input", id, name, cssClass, attributes ++ Map("type" -> "text"), content, events) {

}