package com.sfxcode.nosql.mongo.database

import java.text.SimpleDateFormat

import better.files.{ File, Resource }
import com.sfxcode.nosql.mongo._
import com.sfxcode.nosql.mongo.TestDatabase.PersonDAO
import com.sfxcode.nosql.mongo.model.Person
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.read
import org.mongodb.scala.Document
import org.specs2.specification.BeforeAll

class DocumentExtensionsSpec extends DatabaseSpec with BeforeAll {

  override def beforeAll(): Unit = {
    PersonDAO.drop().result()
    PersonDAO.importJsonFile(File(Resource.getUrl("json/people.json"))).result()
  }

  "Document" should {

    "be converted to plain scala map" in {
      val document: Document = PersonDAO.Raw.find(Map("id" -> 11)).result()

      val map: Map[String, Any] = document.asPlainMap
      map("id") mustEqual 11

      val tags = map("tags").asInstanceOf[List[String]]
      tags must haveSize(7)

      tags.head mustEqual "occaecat"
    }

    "be converted to plain json " in {
      val document: Document = PersonDAO.Raw.find(Map("id" -> 11)).result()

      val s = document.asPlainJson
      implicit val formats = DefaultFormats
      val person: Person = read[Person](s)
      person.id mustEqual 11

      val tags = person.tags
      tags must haveSize(7)

      tags.head mustEqual "occaecat"

    }

  }

}