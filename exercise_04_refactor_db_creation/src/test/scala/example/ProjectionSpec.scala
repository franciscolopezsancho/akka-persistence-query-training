package example

import akka.NotUsed
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.persistence.query.PersistenceQuery
import akka.stream.alpakka.slick.scaladsl.{Slick, SlickSession}
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.config.ConfigFactory
import example.Box._
import org.scalatest.concurrent.PatienceConfiguration
import org.scalatest.wordspec.AnyWordSpecLike
import slick.basic.DatabaseConfig
import slick.dbio.DBIO
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration._

class ProjectionSpec
  extends ScalaTestWithActorTestKit(ConfigFactory.load())
    with AnyWordSpecLike {

  val logger = LoggerFactory.getLogger(this.getClass.getName)

  def randomId = "box" + scala.util.Random.nextInt(Int.MaxValue)

  "A projection" should {
    "be able to write to produce a new table" in {

      val boxId = randomId

      val maxCapacity = 10
      val cart = testKit.spawn(Box(boxId, maxCapacity))
      val probe = testKit.createTestProbe[Confirmation]()
      cart ! AddItem("bar", 1, probe.ref)
      probe.expectMessage(Accepted(9))

      val query = PersistenceQuery(system)
        .readJournalFor[JdbcReadJournal](JdbcReadJournal.Identifier)

      val db = DatabaseConfig.forConfig[JdbcProfile](
        "akka-persistence-jdbc.shared-databases.slick"
      )
      implicit val slickSession: SlickSession = SlickSession.forConfig(db)

      val source: Source[String, NotUsed] =
        query.persistenceIds()

      source.runWith(
        // add an optional first argument to specify the parallelism factor (Int)
        Slick.sink(id => insertEvent(id))
      )

      eventually(PatienceConfiguration.Timeout(10.seconds)) {
        val future = Slick
          .source(sql"select * from my_projection".as[String])
          .runWith(Sink.seq)
        val result = Await.result(future, 1.second)
        logger.info(s"#################### $result")
        result should contain(s"Box|$boxId")
      }
    }

    def insertEvent(event: String): DBIO[Int] =
      sqlu"INSERT INTO my_projection VALUES($event)"

  }

}
