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
import org.slf4j.LoggerFactory

class ProjectionSpec extends ScalaTestWithActorTestKit(ConfigFactory.load()) with AnyWordSpecLike {

   val logger = LoggerFactory.getLogger(this.getClass().getName())

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
    
  

  
}
