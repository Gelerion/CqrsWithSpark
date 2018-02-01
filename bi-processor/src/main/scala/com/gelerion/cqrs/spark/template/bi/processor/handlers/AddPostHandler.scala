package com.gelerion.cqrs.spark.template.bi.processor.handlers

import java.time.{LocalDateTime, ZoneId}

import com.gelerion.cqrs.spark.template.bi.processor.domain.{Post, Repository}
import com.gelerion.cqrs.spark.template.bi.processor.handlers.AbstractHandler.Handler
import com.gelerion.cqrs.spark.template.common.commands.impl.AddPost

/**
  * Created by denis.shuvalov on 31/01/2018.
  */
abstract class AddPostHandler extends Handler[AddPost] {

  val repository: Repository[Post]

  override def apply(cmd: AddPost): Unit = {

    val post = Post(
      cmd.id(),
      LocalDateTime.now(ZoneId.systemDefault()).toString,
      cmd.getAuthor,
      cmd.getMessage
    )

    repository.add(post)
  }

}
