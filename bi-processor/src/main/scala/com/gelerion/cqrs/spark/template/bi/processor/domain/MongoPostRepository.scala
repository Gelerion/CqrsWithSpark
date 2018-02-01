package com.gelerion.cqrs.spark.template.bi.processor.domain

import com.mongodb.MongoClient
import org.bson.Document

/**
  * Created by denis.shuvalov on 31/01/2018.
  */
abstract class MongoPostRepository extends Repository[Post] {

  import  MongoPostRepository._

  val collectionPath: String
  val mongo: MongoClient

  override def add(obj: Post): Unit = {
    val (database, collection) = getDatabase(collectionPath)
    collection.insertOne(obj.toDocument())
  }

  private def getDatabase(collectionPath: String) = {
    val Array(databaseName, collectionName) = collectionPath.split("\\.")
    val database = this.mongo.getDatabase(databaseName)
    val collection = database.getCollection(collectionName)
    (database, collection)
  }

}

object MongoPostRepository {
  val ID      = "_id"
  val DATE    = "date"
  val AUTHOR  = "author"
  val MESSAGE = "message"

  implicit class DocumentConverter(row: Post) {

    def toDocument(): Document = {
      val fields = new Document
      fields.append(ID, row.id)
      fields.append(DATE, row.date)
      fields.append(AUTHOR, row.author)
      fields.append(MESSAGE, row.message)
      fields
    }

  }

}
