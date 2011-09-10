package org.braingrow.server.content.html

import org.braingrow.server.command.Command

abstract case class AbstractHtmlElement(override val elementType: String, id: String, name:String, cssClass: String, atts: Map[String, String], content: List[HtmlContent], events: Map[String, List[Command]])
  extends HtmlContent(elementType) {
  override def asMap: Map[String, Object] = {
    val safeEvents = if (events == null) Map() else events
    val safeContent = if (content == null) List() else content
    val safeAttributes = if (atts == null) Map() else atts
    super.asMap ++ Map("id" -> id, "name" -> name, "cssClass" -> cssClass, "attributes" -> safeAttributes, "content" -> safeContent.map(_.asMap), "events" -> safeEvents.mapValues(_.map(_.asMap)))
  }
}