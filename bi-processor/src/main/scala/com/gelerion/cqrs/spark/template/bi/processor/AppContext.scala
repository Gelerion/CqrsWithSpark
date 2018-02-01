package com.gelerion.cqrs.spark.template.bi.processor

import java.util.Date

import org.apache.kafka.common.serialization.StringSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.gelerion.cqrs.spark.template.bi.processor.config.Config
import com.gelerion.cqrs.spark.template.bi.processor.domain.{MongoPostRepository, Post, Repository}
import com.gelerion.cqrs.spark.template.bi.processor.handlers.AddPostHandler
import com.gelerion.cqrs.spark.template.common.message.bus.MessageBus
import com.mongodb.MongoClient
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.springframework.kafka.core.{DefaultKafkaProducerFactory, KafkaTemplate}
import scala.concurrent.duration._
import com.gelerion.cqrs.spark.template.bi.processor.infrastructure.Utils._

/**
  * Created by denis.shuvalov on 31/01/2018.
  */
object AppContext extends Config {

  lazy val sparkConf: SparkConf = new SparkConf().
    setMaster("local[*]").
    set("spark.ui.enabled", "false").
    set("spark.app.id", new Date().toString).
    setAppName("command-processor")

  lazy val kafkaContext = topics -> Map("metadata.broker.list" -> brokers)

  lazy val mongo = new MongoClient(mongoHost)

  lazy val repository = new MongoPostRepository {
    override lazy val mongo: MongoClient = AppContext.mongo
    override lazy val collectionPath: String = AppContext.collectionPath
  }

  lazy val addPostHandler = new AddPostHandler {
    override val repository: Repository[Post] = AppContext.repository
  }

  lazy val kafkaProducer = {
    val props = new java.util.HashMap[String, AnyRef]
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    val producerFactory = new DefaultKafkaProducerFactory[String, String](props)
    new KafkaTemplate[String, String](producerFactory)
  }

  lazy val messageBus = new MessageBus(kafkaProducer, new ObjectMapper()) {
    eventsTopic = AppContext.eventsTopic
  }

  lazy val cmdProcessor = new CmdProcessor {
    override lazy val topics: Set[String] = kafkaContext._1
    override lazy val params: Map[String, String] = kafkaContext._2

    override lazy val messageBus: MessageBus = AppContext.messageBus
    override lazy val addPostHandler: AddPostHandler = AppContext.addPostHandler
  }

  def buildNewStreamingContext(): () => StreamingContext = () => {
    val ssc = new StreamingContext(sparkConf, 1000.millis) //implicit conversion to spark duration
    AppContext.cmdProcessor.runPipeline(ssc)
    ssc.checkpoint(checkpointDirectory)
    ssc
  }

  lazy val ssc: StreamingContext = {
    StreamingContext.getOrCreate(checkpointDirectory, buildNewStreamingContext())
  }

  class MainApp extends App {
    ssc.start()
    ssc.awaitTermination()
  }
}

/**
  * Major entry point for the application
  */
object MainApp extends AppContext.MainApp
