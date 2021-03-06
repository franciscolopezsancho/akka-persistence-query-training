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

object DBFactory{

  val db = DatabaseConfig.forConfig[JdbcProfile](
    "akka-persistence-jdbc.shared-databases.slick"
  )
  val slickSession: SlickSession = SlickSession.forConfig(db)

  def createProjections(projectionTables: List[String]): Unit = {
    Await.ready(slickSession.db.run(dropProjections(projectionTables)), 30.seconds)
    projectionTables.map{tableName => 
      Await.ready(slickSession.db.run(createProjection(tableName)), 30.seconds)
    }
  }

  def createDefaultTables: Unit = {
    Await.ready(slickSession.db.run(dropDefaultTables), 30.seconds)
    Await.ready(slickSession.db.run(createJournal), 30.seconds)
    Await.ready(slickSession.db.run(createSnapshot), 30.seconds)
  }

  def dropDefaultTables: DBIO[Int] = sqlu"""
    DROP TABLE IF EXISTS journal, snapshot;
    """ 

  def dropProjections(projectionTableNames: List[String]): DBIO[Int] = sqlu"""
    DROP TABLE IF EXISTS #${projectionTableNames.mkString(", ")};
    """

  def createProjection(tableName: String): DBIO[Int] = sqlu"""
    CREATE TABLE IF NOT EXISTS #$tableName (
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