package org.braingrow.server.command

trait Command {
  def asMap: Map[String, Object]
}