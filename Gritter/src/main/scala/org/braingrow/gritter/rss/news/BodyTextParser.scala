package org.braingrow.gritter.rss.news

import org.apache.xerces.xni.{XMLString, Augmentations, XMLAttributes, QName}

/**
 * User: ibogemann
 * Date: 24/09/11
 * Time: 14:11
 */

class BodyTextParser extends ContentReadingParser {
  var content = "";
  var state = ""


  var savedState = ""

  def clearState() {
    var content = "";
    var state = ""
    var savedState = ""
  }

  override def startElement(p1: QName, p2: XMLAttributes, p3: Augmentations) {
    p1.localpart match {

      case "BODY" => state = "CONTENT"
      case _ => {}
    }
  }

  override def endElement(p1: QName, p2: Augmentations) {
    p1.localpart match {
      case "BODY" => state = ""
      case _ => {}
    }
  }

  override def characters(p1: XMLString, p2: Augmentations) {
    state match {
      case "CONTENT" => content = content + " " + p1.toString
      case _ => {}
    }
  }
}