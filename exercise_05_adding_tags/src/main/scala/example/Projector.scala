package example

import akka.actor.typed.ActorSystem
import akka.persistence.query.PersistenceQuery
import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.stream.alpakka.slick.scaladsl.SlickSession
import akka.stream.scaladsl.Source
import akka.NotUsed
import akka.stream.alpakka.slick.scaladsl.Slick
import slick.dbio.DBIO
import slick.jdbc.MySQLProfile.api._


object Projector {

  def init(tag: String, system: ActorSystem[_]) = {
    val query = PersistenceQuery(system)
      .readJournalFor[JdbcReadJournal](JdbcReadJournal.Identifier)

    implicit val materializer = system

    implicit val slickSession: SlickSession = DBHandler.slickSession

    val source: Source[String,NotUsed] =
      query.persistenceIds()

    source.runWith(
      // add an optional first argument to specify the parallelism factor (Int)
      Slick.sink(ids => insertEvent(ids))
    )
  }

  def insertEvent(event: String): DBIO[Int] =
    sqlu"INSERT INTO projection VALUES($event)"

}
