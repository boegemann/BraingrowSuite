package org.braingrow.gritter.drools

import org.drools.base.accumulators.AccumulateFunction
import twitterutil.IdentifiableText
import java.io.{Externalizable, ObjectOutput, ObjectInput, Serializable}
import java.lang.Object

/**
 * User: ibogemann
 * Date: 10/09/11
 * Time: 20:25
 */

class Top10 extends AccumulateFunction {

  val obj = new Object()

  class Context extends Externalizable {
    var colAll: List[IdentifiableText] = List.empty

    def writeExternal(out: ObjectOutput) {
      out.writeObject(colAll)
    }

    def readExternal(in: ObjectInput) {
      colAll = in.readObject().asInstanceOf[List[IdentifiableText]]
    }
  }

  def createContext() = new Context()

  def init(context: Serializable) {
    context.asInstanceOf[Context].colAll = List.empty
  }

  def accumulate(context: Serializable, identifiableText: AnyRef) {
    obj.synchronized(
      context.asInstanceOf[Context].colAll = context.asInstanceOf[Context].colAll.+:(identifiableText.asInstanceOf[IdentifiableText])
    )
  }

  def reverse(p1: Serializable, p2: AnyRef) {}

  def getResult(context: Serializable) = {
    context.asInstanceOf[Context].colAll.groupBy(_.text).toList.sortWith(
      // order by size unless they are the same size, then order alphabetical by word
      (it1, it2) => if (it1._2.size == it2._2.size) it1._1.size < it2._1.size else it1._2.size > it2._2.size
    ).slice(0, 10)
  }

  def supportsReverse() = false

  def writeExternal(out: ObjectOutput) {}

  def readExternal(in: ObjectInput) {}
}