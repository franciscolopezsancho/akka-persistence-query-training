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


  "The events from the Shopping Cart" should {
    "be consumed by the event processor" in {

      val boxId = scala.util.Random.nextInt(Int.MaxValue).toString

      DBHandler.createTables

      Projector.init("box", system)

      val cart = testKit.spawn(Box(boxId,10))
      val probe = testKit.createTestProbe[Box.Confirmation]
      cart ! Box.AddItem("fooo", 2, probe.ref)
      probe.expectMessage(Box.Accepted(roomLeft = 8))

      implicit val session = DBHandler.slickSession


      eventually(PatienceConfiguration.Timeout(10.seconds)) {
        val future = Slick
          .source(sql"select * from projection".as[String])
          .runWith(Sink.seq)
          val result = Await.result(future,1.second)
          result should contain(s"ItemAdded($boxId,fooo,2)")
      }

    }

  }

}
