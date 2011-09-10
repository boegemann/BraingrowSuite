package org.braingrow.gritter.twitter

import java.text.SimpleDateFormat
import net.liftweb.json.JsonAST.{JString, JValue}
import java.util.Date

/**
 * User: ibogemann
 * Date: 09/09/11
 * Time: 20:03
 */

trait JsonReader {
  private val dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")

  protected def asText(obj: JValue): String = {
    obj match {
      case JString(text) => text
      case _ => ""
    }
  }

  protected def asDate(obj: JValue): Date = {
    obj match {
      case JString(text) => dateFormat.parse(text)
      case _ => null
    }
  }
}