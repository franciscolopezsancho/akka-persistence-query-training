package example

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import org.scalatest.wordspec.AnyWordSpecLike
import Box._
import com.typesafe.config.ConfigFactory
import akka.persistence.query.PersistenceQuery
import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import akka.stream.alpakka.slick.scaladsl.SlickSession
import akka.stream.scaladsl.Source
import akka.persistence.query.EventEnvelope
import akka.NotUsed
import akka.stream.alpakka.slick.scaladsl.Slick
import slick.dbio.DBIO
import slick.jdbc.MySQLProfile.api._
import org.scalatest.concurrent.PatienceConfiguration
import akka.stream.scaladsl.Sink
import scala.concurrent.Await
import scala.concurrent.duration._

class ProjectionSpec
    extends ScalaTestWithActorTestKit(ConfigFactory.load())
    with AnyWordSpecLike {


      val tableNames = List("projection1","projection2","projection3")

      DBHandler.dropTables(tableNames)
      DBHandler.createTables(tableNames)


  // "The events from the Box Cart" should {
  //   "be consumed by the event processor that corresponds to its tag" in {

  //     def calculateTag(paralellism: Int, entityId: String): Int = {
  //       entityId.toInt % paralellism
  //     }

  //     /// For tag zero   
  //     val box0Id = 2.toString() // this will be tagged with 0, see assert below
  //     val box0 = (Box(box0Id,10))
  //     assert(calculateTag(2,box0Id) == 0)
  //    // For tag 1
  //      val box1Id = 1.toString() // this will be tagged with 0, see assert below
  //     val box1 = (Box(box1Id,9))
  //     assert(calculateTag(2,box1Id) == 1)

      
  //     val cart0 = testKit.spawn(box0)
  //     val probe0 = testKit.createTestProbe[Box.Confirmation]
  //     cart0 ! Box.AddItem("f000", 2, probe0.ref)
  //     probe0.expectMessage(Box.Accepted(roomLeft = 8))
      
  //     val cart1 = testKit.spawn(box1)
  //     val probe1 = testKit.createTestProbe[Box.Confirmation]
  //     cart1 ! Box.AddItem("f111", 2, probe1.ref)
  //     probe1.expectMessage(Box.Accepted(roomLeft = 7))
      
  //     val projection1TableName = tableNames(1) 

  //     val projection0TableName = tableNames(0)

     

  //     Projector.init("box-tag-0", system, projection0TableName)


  //     Projector.init("box-tag-1", system, projection1TableName)

     

  //     implicit val session = DBHandler.slickSession

  //     eventually(PatienceConfiguration.Timeout(3.seconds)) {
  //       val future = Slick
  //         .source(sql"select * from #$projection0TableName".as[String])
  //         .runWith(Sink.seq)
  //         val result = Await.result(future,1.second)
  //         result should contain(s"ItemAdded($box0Id,f000,2)")
  //         result should not contain(s"ItemAdded($box1Id,f111,2)")
  //     }

  //     eventually(PatienceConfiguration.Timeout(3.seconds)) {
  //       val future = Slick
  //         .source(sql"select * from #$projection1TableName".as[String])
  //         .runWith(Sink.seq)
  //         val result = Await.result(future,1.second)
  //         result should not contain(s"ItemAdded($box0Id,f000,2)")
  //         result should contain(s"ItemAdded($box1Id,f111,2)")
  //     }

  //   }

  // }

   "Only the cleaned events from the Box Cart" should {
    "should be consumed" in {


       val boxId = scala.util.Random.nextInt(Int.MaxValue).toString

      val projectionTableName = "projection3"

      Projector.init("box-cleaned", system, projectionTableName)

      val cart = testKit.spawn(Box(boxId,10))
      val probe = testKit.createTestProbe[Box.Confirmation]
      cart ! Box.AddItem("fiii", 2, probe.ref)
      probe.expectMessage(Box.Accepted(roomLeft = 8))
      cart ! Box.CleanBox( probe.ref)
      probe.expectMessage(Box.Accepted(roomLeft = 10))

      implicit val session = DBHandler.slickSession

      eventually(PatienceConfiguration.Timeout(3.seconds)) {
        val future = Slick
          .source(sql"select * from #$projectionTableName".as[String])
          .runWith(Sink.seq)
          val result = Await.result(future,1.second)
          result should not contain(s"ItemAdded($boxId,fiii,2)")
          result should contain(s"BoxCleaned($boxId)")
      }

    }

  }

}
