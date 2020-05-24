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
import akka.persistence.query.Offset
import akka.persistence.query.EventEnvelope


object Projector {

  def init(tag: String, system: ActorSystem[_]) = {
    val query = PersistenceQuery(system)
      .readJournalFor[JdbcReadJournal](JdbcReadJournal.Identifier)

    implicit val materializer = system

    implicit val slickSession: SlickSession = DBHandler.slickSession

    val source: Source[EventEnvelope,NotUsed] =
      query.eventsByTag(tag, Offset.noOffset)

      

    source.runWith(
      // add an optional first argument to specify the parallelism factor (Int)
      Slick.sink(event => insertEvent(event.event.toString()))
    )
  }

  def insertEvent(event: String): DBIO[Int] =
    sqlu"INSERT INTO projection VALUES($event)"

}
