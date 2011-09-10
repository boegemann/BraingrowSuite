package org.braingrow.server.content.html

import scala.reflect.BeanInfo

@BeanInfo
case class HtmlContent(val elementType: String) {


  def asMap: Map[String, Object] = {
    Map("elementType" -> elementType)
  }

}