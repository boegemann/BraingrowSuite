package org.braingrow.server.content.html

import scala.reflect.BeanInfo
@BeanInfo
case class TextContent(text: String) extends HtmlContent("text") {
  override def asMap: Map[String, Object] = {
    super.asMap ++ Map("text" -> text)
  }
}

