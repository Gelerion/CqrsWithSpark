package com.gelerion.cqrs.spark.template.bi.processor.handlers

/**
  * Created by denis.shuvalov on 31/01/2018.
  */
object AbstractHandler {

  type Handler[T] = T => Unit

}
