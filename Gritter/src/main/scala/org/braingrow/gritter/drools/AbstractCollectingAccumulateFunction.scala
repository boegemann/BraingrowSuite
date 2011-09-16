package org.braingrow.gritter.drools

import java.io.{ObjectInput, ObjectOutput, Externalizable, Serializable}
import org.drools.base.accumulators.AccumulateFunction

/**
 * User: ibogemann
 * Date: 15/09/11
 * Time: 20:29
 */

class Context extends Externalizable {
  var colAll: List[AnyRef] = List.empty

  def writeExternal(out: ObjectOutput) {
    out.writeObject(colAll)
  }

  def readExternal(in: ObjectInput) {
    colAll = in.readObject().asInstanceOf[List[AnyRef]]
  }
}

abstract class AbstractCollectingAccumulateFunction extends AccumulateFunction {
  val obj = new Object()


  def createContext() = new Context()

  def init(context: Serializable) {
    context.asInstanceOf[Context].colAll = List.empty
  }

  def accumulate(context: Serializable, fact: AnyRef) {
    // simply add to the list inside our Context object
    obj.synchronized {
      context.asInstanceOf[Context].colAll =
        context.asInstanceOf[Context].colAll.+:(fact)
    }
  }

  def reverse(p1: Serializable, p2: AnyRef) {}

  def getResult(context: Serializable): Object


  def supportsReverse() = false

  def writeExternal(out: ObjectOutput) {}

  def readExternal(in: ObjectInput) {}
}