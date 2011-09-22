package org.braingrow.gritter.twitter

import dispatch.{futures, Http, Request}
import net.liftweb.json._
import akka.actor.ActorRef

/**
 * User: ibogemann
 * Date: 08/09/11
 * Time: 10:13
 */

class TwitterStreamListener {
  def startListener(streamRequest: Request, handler: ActorRef) {
    val http = new Http

    futures.DefaultFuture.future {
      http(streamRequest >> {
        (stm, charset) => {
          import java.io._
          println("start")
          val reader: BufferedReader = new BufferedReader(new InputStreamReader(stm, charset))
          var line = reader.readLine().replaceAll("\0", "")
          while (line != null) {
            try {
              line = reader.readLine().replaceAll("\0", "")
              if (line != null && line.trim.length > 0) {
                val jValue = parse(line)
                val statusEvent = StatusEvent.fromJson(jValue)
                handler ! statusEvent
              }
            } catch {
              case ex: Exception => ex.printStackTrace()
            }
          }
          stm.close()
        }
      })
    }
  }
}