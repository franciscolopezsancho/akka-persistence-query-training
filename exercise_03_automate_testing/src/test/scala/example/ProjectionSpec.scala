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
import org.slf4j.LoggerFactory


class ProjectionSpec extends ScalaTestWithActorTestKit(ConfigFactory.load()) with AnyWordSpecLike {


 val logger = LoggerFactory.getLogger(classOf[ProjectionSpec])

  "A projection" should {
    "be able to read from the journal and println" in {
       val query = PersistenceQuery(system)
        .readJournalFor[JdbcReadJournal](JdbcReadJournal.Identifier)

      val source: Source[String, NotUsed] =
        query.persistenceIds()

      source.runForeach(each => logger.info(s"################# $each"))
      Thread.sleep(3000)
    }
  }
    
  "A projection" should {
    "be able to write to produce a new table" in  {
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
      
      Thread.sleep(3000)
      assert(fail())//TODO check in here that we actually have those values in 'my_projection' table  
  }

   def insertEvent(event: String): DBIO[Int] =
      sqlu"INSERT INTO my_projection VALUES($event)"

    
  }
  

  
}
