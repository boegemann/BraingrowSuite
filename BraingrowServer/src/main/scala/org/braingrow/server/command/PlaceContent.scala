package org.braingrow.server.command

import org.braingrow.server.content.html.HtmlContent
case class PlaceContent(content: HtmlContent, location: String) extends Command {
  implicit val formats = net.liftweb.json.DefaultFormats

  def asMap: Map[String, Object] = Map("cmd" -> "PlaceContent", "location" -> location, "content" -> content.asMap)
}