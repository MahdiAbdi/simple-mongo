# Reactive Streams

* The MongoDB Scala driver is built upon [Reactive Streams](https://www.reactive-streams.org/)
* simple-mongo wraps around the scala driver => Full support of Reactive Streams
* For Blocking Results (implicit) conversion to Result Objects is provided
* Conversion of Observable to Future is available


@@@ note { title=ScalaDriverDocs }

The MongoDB Scala Driver is an asynchronous and non blocking driver. Using the Observable model asynchronous events become simple, composable operations, freed from the complexity of nested callbacks.

Additional Info for [Reactive Streams JVM](https://github.com/reactive-streams/reactive-streams-jvm/)

Additional Info for [Mongo Scala Reactive Streams](https://mongodb.github.io/mongo-java-driver/4.0/driver-scala/getting-started/quick-start-primer/)



@@@

## Blocking Results

Conversion is provided in the DAO instances with four Helper function for Observable[C]:

| Function Name | Function Result | Sample                                         | Sample Result  |
|:--------------|:----------------|:-----------------------------------------------|:---------------|
| result        | C               | BookDAO.count().result()                       | Long           |
| results       | Seq[C]          | PersonDAO.findAggregated(aggregator).results() | Seq[Person]    |
| resultList    | List[C]         | UserDAO.find("name", "User").resultList()      | List[User]     |
| resultOption  | Option[C]       | PersonDAO.find(Map("id" -> 42)).resultOption() | Option[Person] |

All functions have an optional maxWait parameter (Default maxWait = 10 seconds).

@@snip [Scala Sources](/docs/src/main/scala/ReactiveStreamsDoc.scala) { #max_wait }

### Implicit Result Conversion (Blocking)

To use implicit result conversion, you have to import the simple mongo base package object.

```scala
import com.sfxcode.nosql.mongo._
```

After that, implicit conversion and other useful implicits (e.g. Map -> Bson) are available.

@@snip [Scala Sources](/docs/src/main/scala/ReactiveStreamsDoc.scala) { #implicit_result_conversion }

## Future Results

DAO Instances support (implicit) conversion to Future,

@@snip [Scala Sources](/docs/src/main/scala/ReactiveStreamsDoc.scala) { #as_future }
