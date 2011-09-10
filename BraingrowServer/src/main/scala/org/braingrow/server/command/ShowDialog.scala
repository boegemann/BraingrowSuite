package org.braingrow.server.command

import org.braingrow.server.content.html.HtmlContent

case class ShowDialog(title: String, content: HtmlContent) extends Command {

  def asMap: Map[String, Object] = Map("cmd" -> "ShowDialog", "title" -> title, "content" -> content.asMap)

}