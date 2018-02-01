package com.gelerion.cqrs.spark.template.bi.processor.config

import com.typesafe.config.{ConfigFactory, Config => TypeSafeConfig}
import scala.collection.JavaConverters._

/**
  * Created by denis.shuvalov on 31/01/2018.
  */
trait Config {

  lazy val mainConfig: TypeSafeConfig = ConfigFactory.load()

  val checkpointDirectory: String = mainConfig.getString("app.kafka.checkpoint-dir")
  val collectionPath: String      = mainConfig.getString("app.mongo.collection-path")
  val mongoHost: String           = mainConfig.getString("app.mongo.host")
  val brokers: String             = mainConfig.getString("app.kafka.brokers")
  val topics: Set[String]         = mainConfig.getStringList("app.kafka.commands-topics").asScala.toSet
  val eventsTopic: String         = mainConfig.getString("app.kafka.events-topic")

}
