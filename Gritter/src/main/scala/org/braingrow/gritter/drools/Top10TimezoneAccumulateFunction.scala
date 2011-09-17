package org.braingrow.gritter.drools

import java.io.Serializable

/**
 * User: ibogemann
 * Date: 10/09/11
 * Time: 20:25
 */

class Top10TimezoneAccumulateFunction extends AbstractCollectingAccumulateFunction {


  def getResult(context: Serializable) = {
    // group all items (creates a Map[String,List]) convert to a list of touples we can then sort
    context.asInstanceOf[Context].colAll.asInstanceOf[List[String]].groupBy(identity).toList.sortWith({
      // order by size unless they are the same size, then order alphabetical by word
      case ((timezone1, list1), (timezone2, list2)) =>
        list1.size > list2.size
    }).map({
      case (t, l) => (t, l.size)
    }).slice(0, 10)
  }

  def getResultType = classOf[List[(String, Int)]]
}
