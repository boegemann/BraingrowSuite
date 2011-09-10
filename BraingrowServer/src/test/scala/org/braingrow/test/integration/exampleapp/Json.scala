package org.braingrow.test.integration.exampleapp

import org.braingrow.server.content.html._
import net.liftweb.json.JsonAST._
import net.liftweb.json.Extraction._
import net.liftweb.json.Printer._
object Json {
  implicit val formats = net.liftweb.json.DefaultFormats

  def main(args: Array[String]): Unit = {
    println(pretty(render(decompose(TextContent("Fred").asMap))))

    val div = Div(id ="id1", cssClass = "css", attributes = Map("width" -> "100px"), content = List(TextContent("Frieda")))
    println(pretty(render(decompose(div.asMap))))
  }
}

