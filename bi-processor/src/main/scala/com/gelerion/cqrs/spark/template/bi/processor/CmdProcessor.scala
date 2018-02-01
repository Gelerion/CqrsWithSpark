package com.gelerion.cqrs.spark.template.bi.processor

import com.gelerion.cqrs.spark.template.bi.processor.handlers.AddPostHandler
import com.gelerion.cqrs.spark.template.common.commands.impl.AddPost
import com.gelerion.cqrs.spark.template.common.events.CmdCompleted
import com.gelerion.cqrs.spark.template.common.message.bus.MessageBus
import kafka.serializer.StringDecoder
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka.KafkaUtils
import org.json4s.{DefaultFormats, _}
import org.json4s.jackson.JsonMethods._

import scala.util.Try
import scala.util.control.NonFatal

/**
  * Created by denis.shuvalov on 31/01/2018.
  */
abstract class CmdProcessor extends Serializable {

  val topics: Set[String]

  val params: Map[String, String]

  // this hack with ```lazy``` and ```@transient``` allows to load [[addPostHandler]]
  // just one time regardless of running kind (restoring from checkpoints or clean start)
  @transient
  val addPostHandler: AddPostHandler

  @transient
  val messageBus: MessageBus

  /**
    * Synthetic method to catch local variables {{localHandler}} and {{messageBus}}
    */
  def runPipeline(ssc: StreamingContext) = {
    val dataStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, params, topics)

    dataStream.foreachRDD { rdd =>
      val values = rdd.values


      values.foreach { cmdMessage =>
        implicit val formats = DefaultFormats
        val ast = parse(cmdMessage)

        // take command type and id
        val baseCmd = (ast \ "type").extract[String]
        val cmdId = (ast \ "id").extract[String]

        // dispatch command to corresponding handler
        Try {
          baseCmd match {
            case AddPost.TYPE =>
              val cmd = ast.extract[AddPost]
              cmd.setId(cmdId)

              addPostHandler(cmd)
          }

          CmdCompleted.builder().id(cmdId).succeeded(true).build()

        }.recover {
          case NonFatal(ex) => CmdCompleted.builder().id(cmdId).succeeded(false).error(ex.getMessage).build()
        }.foreach { event =>
          messageBus.send(event)
        }
      }
    }
  }
}
