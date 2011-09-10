package org.braingrow.server.command

import org.braingrow.server.content.html.HtmlContent

case class SetData(ownerQuery:String = null, slot:String,data:Map[String,Object])  extends Command {

  def asMap: Map[String, Object] = Map("cmd" -> "SetData", "ownerQuery" -> ownerQuery, "slot" -> slot, "data" -> data)
}