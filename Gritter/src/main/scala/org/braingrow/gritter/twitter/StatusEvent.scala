package org.braingrow.gritter.twitter

import net.liftweb.json.JsonAST.{JValue, JString}
import java.util.Date
import java.text.{SimpleDateFormat, DateFormat}

/**
 * User: ibogemann
 * Date: 06/09/11
 * Time: 18:19
 */

abstract class StatusEvent {
  val createdAt: Date
  val idStr: String
  val text: String
  val source: String
  val truncated: String
  val inReplyToStatusId: Option[String]
  val inReplyToUserId: Option[String]
  val favorited: String
  val user: User
}

object StatusEvent extends JsonReader {


  def fromJson(json: JValue): StatusEvent = {
    new StatusEvent {
      val createdAt = asDate(json \ "created_at")
      val idStr = asText(json \ "id_str")
      val text = asText(json \ "text")
      val source = asText(json \ "source")
      val truncated = asText(json \ "truncated")
      val inReplyToStatusId =
        if (asText(json \ "in_reply_to_status_id") != "")
          Some(asText(json \ "in_reply_to_status_id"))
        else
          None
      val inReplyToUserId =
        if (asText(json \ "in_reply_to_user_id") != "")
          Some(asText(json \ "in_reply_to_user_id"))
        else
          None
      val favorited = asText(json \ "favorited")
      val user = User.fromJson(json \ "user")
    }
  }


}