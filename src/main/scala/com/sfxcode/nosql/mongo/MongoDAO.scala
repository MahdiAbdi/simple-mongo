package com.sfxcode.nosql.mongo

import java.nio.charset.Charset

import better.files.File
import com.sfxcode.nosql.mongo.bson.DocumentHelper
import com.sfxcode.nosql.mongo.database.{ChangeObserver, CollectionStatus, DatabaseProvider}
import com.sfxcode.nosql.mongo.operation.Crud
import org.bson.json.JsonParseException
import org.mongodb.scala.{BulkWriteResult, Document, MongoCollection, Observable, SingleObservable}

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

/**
  * Created by tom on 20.01.17.
  */
abstract class MongoDAO[A](provider: DatabaseProvider, collectionName: String)(implicit ct: ClassTag[A])
    extends Crud[A] {

  val databaseName: String = provider.guessDatabaseName(collectionName)

  val name: String = provider.guessName(collectionName)

  val collection: MongoCollection[A] = provider.collection[A](collectionName)

  def addChangeObserver(observer: ChangeObserver[A]): ChangeObserver[A] = {
    coll.watch[A]().subscribe(observer)
    observer
  }

  def collectionStatus: Observable[CollectionStatus] =
    provider.runCommand(Map("collStats" -> collectionName)).map(document => CollectionStatus(document))

  protected def coll: MongoCollection[A] = collection

  // internal object for raw document access
  object Raw extends MongoDAO[Document](provider, collectionName)

  def importJsonFile(file: File): SingleObservable[BulkWriteResult] = {
    val docs = new ArrayBuffer[Document]()
    try {
      if (file.exists) {
        val iterator = file.lineIterator(Charset.forName("UTF-8"))
        iterator.foreach(line => docs.+=(DocumentHelper.documentFromJsonString(line).get))
      }
    }
    catch {
      case e: JsonParseException =>
        logger.error(e.getMessage, e)
    }
    Raw.bulkWriteMany(docs.toSeq)
  }

  override def toString: String = "%s:%s@%s, %s".format(databaseName, collectionName, provider.config, super.toString)
}
