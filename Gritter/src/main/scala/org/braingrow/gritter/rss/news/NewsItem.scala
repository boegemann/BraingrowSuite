package org.braingrow.gritter.rss.news

import java.util.Date

/**
 * User: ibogemann
 * Date: 23/09/11
 * Time: 16:15
 */

case class NewsItem(publishedDate: Date, updatedDate: Option[Date], title: String, description: String, link: String, content: String)