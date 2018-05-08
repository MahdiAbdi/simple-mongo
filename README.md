# simple-mongo


A small library for easy usage of the mongo-scala-driver.

## Documentation

Documentation can be found [here](https://sfxcode.github.io/simple-mongo/).



## Version

Scala Version is 2.12.x / 2.11.x.

## Travis

[![Build Status](https://travis-ci.org/sfxcode/simple-mongo.svg?branch=master)](https://travis-ci.org/sfxcode/simple-mongo)

## Download

[ ![Download](https://api.bintray.com/packages/sfxcode/maven/simple-mongo/images/download.svg) ](https://bintray.com/sfxcode/maven/simple-mongo/_latestVersion)

## Licence

[Apache 2 License](https://github.com/sfxcode/simple-mongo/blob/master/LICENSE).


## Usage

Add following lines to your build.sbt

```
resolvers += "sxfcode Bintray Repo" at "https://dl.bintray.com/sfxcode/maven/"

libraryDependencies += "com.sfxcode.nosql" %% "simple-mongo" % "1.0.4"

```

Define MongoDB Connection and [DAO](https://en.wikipedia.org/wiki/Data_access_object) objects for automatic case class conversion.


```scala

import java.util.Date

import com.sfxcode.nosql.mongo.MongoDAO
import com.sfxcode.nosql.mongo.database.DatabaseProvider
import org.bson.codecs.configuration.CodecRegistries._
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.bson.codecs.Macros._

/**
 * import mongodb restaurants sample data
 */
object RestaurantDatabase {

  case class Address(street: String, building: String, zipcode: String, coord: List[Double])

  case class Grade(date: Date, grade: String, score: Int)

  case class Restaurant(restaurant_id: String, name: String, borough: String, cuisine: String,
    grades: List[Grade], address: Address, _id: ObjectId = new ObjectId())

  private val registry = fromProviders(classOf[Restaurant], classOf[Address], classOf[Grade])

  val database = DatabaseProvider("test", registry)

  object RestaurantDAO extends MongoDAO[Restaurant](database, "restaurants")

}


```


Import the database object and execute find and CRUD functions on the DAO object.

```scala
 import RestaurantDatabase._
 
 import com.sfxcode.nosql.mongo._
 
 object RestaurantApp extends App {
 
   val restaurant: Option[Restaurant] = RestaurantDAO.find("name", "Dj Reynolds Pub And Restaurant")
 
   println(restaurant.get.grades)
 
   val restaurants: List[Restaurant] = RestaurantDAO.find(Map("address.zipcode" -> "10075", "cuisine" -> "Italian"))
 
   restaurants.sortBy(r => r.name).foreach(r => println(r.name))
 
 }

```




