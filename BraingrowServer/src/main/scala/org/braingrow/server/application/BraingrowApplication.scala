package org.braingrow.server.application

import java.util.UUID
import org.braingrow.server.BraingrowJsonSocket
import org.braingrow.server.BraingrowWebsocketAcceptor

trait BraingrowApplication {
  val token = UUID.randomUUID().toString()

  def title: String

  def createJsonSocket(acceptor: BraingrowWebsocketAcceptor): BraingrowJsonSocket;
}