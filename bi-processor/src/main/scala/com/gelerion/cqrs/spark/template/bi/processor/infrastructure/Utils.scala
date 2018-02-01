package com.gelerion.cqrs.spark.template.bi.processor.infrastructure


import org.apache.spark.streaming.{Duration => SparkDuration}

import scala.concurrent.duration.Duration

/**
  * Created by denis.shuvalov on 31/01/2018.
  */
object Utils {

  implicit def scalaDurationToSparkDuration(time: Duration): SparkDuration =
    new SparkDuration(time.toMillis)

}
