package org.braingrow.gritter.drools.twitterutil

/**
 * User: ibogemann
 * Date: 10/09/11
 * Time: 14:43
 */

class DynamicSizeRegulator {
  @volatile
  var curValue = 2;

  def train(value: Int) {
    if (value > 18) {
      curValue += 1
      println("increased to:" + curValue)
    }
    if (value < 12 && curValue > 2) {
      curValue -= 1
      println("decreased to:" + curValue)
    }
  }

  def getMinCount():Int = curValue;
}