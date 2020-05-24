package example

import com.typesafe.config.ConfigFactory
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.Sink
import akka.pattern.pipe
import akka.actor.typed.ActorSystem
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import akka.stream.alpakka.slick.scaladsl.SlickSession

object DBHandler{

  val db = DatabaseConfig.forConfig[JdbcProfile](
    "akka-persistence-jdbc.shared-databases.slick"
  )
  val slickSession: SlickSession = SlickSession.forConfig(db)

  def createTables(): Unit = {
    Await.ready(slickSession.db.run(dropTables), 30.seconds)
    Await.ready(slickSession.db.run(createJournal), 30.seconds)
    Await.ready(slickSession.db.run(createProjection), 30.seconds)
    Await.ready(slickSession.db.run(createSnapshot), 30.seconds)
  }

  def dropTables: DBIO[Int] = sqlu"""
    DROP TABLE IF EXISTS journal, snapshot, projection;
    """

  val createProjection: DBIO[Int] = sqlu"""
    CREATE TABLE IF NOT EXISTS projection (
        event VARCHAR(255) NOT NULL
    );
    """

  val createSnapshot: DBIO[Int] = sqlu"""
    CREATE TABLE IF NOT EXISTS snapshot (
      persistence_id VARCHAR(255) NOT NULL,
     sequence_number BIGINT NOT NULL,
      created BIGINT NOT NULL,
      snapshot BLOB NOT NULL,
      PRIMARY KEY (persistence_id, sequence_number)
    );
    """

  val createJournal: DBIO[Int] = sqlu"""
    CREATE TABLE IF NOT EXISTS journal (
      ordering SERIAL,
      persistence_id VARCHAR(255) NOT NULL,
      sequence_number BIGINT NOT NULL,
      deleted BOOLEAN DEFAULT FALSE,
      tags VARCHAR(255) DEFAULT NULL,
      message BLOB NOT NULL,
      PRIMARY KEY(persistence_id, sequence_number)
    );
    """
}