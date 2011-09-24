package org.braingrow.gritter.rss.news

import org.cyberneko.html.parsers.SAXParser
import org.apache.xerces.xni.{Augmentations, NamespaceContext, XMLLocator}

/**
 * User: ibogemann
 * Date: 24/09/11
 * Time: 11:04
 */

trait ContentReadingParser extends SAXParser {

  override def startDocument(p1: XMLLocator, p2: String, p3: NamespaceContext, p4: Augmentations) {
    clearState()
  }

  def clearState()

  def content: String
}