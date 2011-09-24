package org.braingrow.gritter.rss.news

import com.sun.syndication.feed.synd.SyndEntry
import dispatch._
import org.xml.sax.InputSource

class ContentGrabbingSyndEntryDigester(forwardFunction: NewsItem => Unit, parser: ContentReadingParser) {

  val AUGMENTATIONS = "http://cyberneko.org/html/features/augmentations";


  def digest(entry: SyndEntry) {

    Http(url(entry.getLink) >>~ {
      reader => {
        parser.parse(new InputSource(reader))

        val textCol = parser.content.split("\\s").map(_.trim).filter(_ != "").mkString(" ")

        forwardFunction(NewsItem(
          entry.getPublishedDate,
          if (entry.getUpdatedDate == null) None else Some(entry.getUpdatedDate),
          entry.getTitle,
          entry.getDescription.getValue,
          entry.getLink,
          // cleanse text of execessive whitespace
          textCol
        ))
      }


    })

  }

}