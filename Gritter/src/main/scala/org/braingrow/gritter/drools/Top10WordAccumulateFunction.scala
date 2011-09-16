package org.braingrow.gritter.drools

import twitterutil.IdentifiableText
import java.io.Serializable

/**
 * User: ibogemann
 * Date: 10/09/11
 * Time: 20:25
 */

class Top10WordAccumulateFunction extends AbstractCollectingAccumulateFunction {


  def getResult(context: Serializable) = {
    // group all items (creates a Map[String,List]) convert to a list of touples we can then sort
    context.asInstanceOf[Context].colAll.asInstanceOf[List[IdentifiableText]].groupBy(_.text).toList.sortWith({
      // order by size unless they are the same size, then order alphabetical by word
      case ((word1, list1), (word2, list2)) =>
        if (list1.size == list2.size)
          word1 < word2
        else
          list1.size > list2.size
    }).slice(0, 10)
  }

}
