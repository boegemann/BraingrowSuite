package org.braingrow.gritter.twitter

import net.liftweb.json.JsonAST.JValue
import java.util.Date


/**
 * User: ibogemann
 * Date: 06/09/11
 * Time: 18:21
 */

abstract class User {
  val idStr: String
  val url: String
  val profileImageUrl: String
  val language: String
  val description: String
  val timeZone: String
  val location: String
  val name: String
  val screenName: String
  val createdAt: Date
}

object User  extends JsonReader {

  def fromJson(json: JValue): User = {
    new User {
      val idStr = asText(json \ "id_str")
      val url = asText(json \ "url")
      val profileImageUrl = asText(json \ "profile_image_url")
      val language = asText(json \ "lang")
      val description = asText(json \ "description")
      val timeZone = asText(json \ "time_zone")
      val location = asText(json \ "location")
      val name = asText(json \ "name")
      val screenName = asText(json \ "screen_name")
      val createdAt = asDate(json \ "created_at")
    }
  }

}