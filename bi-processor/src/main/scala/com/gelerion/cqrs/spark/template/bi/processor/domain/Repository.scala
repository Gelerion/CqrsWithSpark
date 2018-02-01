package com.gelerion.cqrs.spark.template.bi.processor.domain

/**
  * Created by denis.shuvalov on 31/01/2018.
  */
trait Repository[T] {

  def add(obj: T): Unit

}
