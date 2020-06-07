#### Refactor all the things

##### TO DO:
- refactor in two classes. 
   - DBFactory
   - Proyector

##### Some hints:

Now that we have an automated test I guess is time for us to clean up. What about you create a couple of classes for that?
I created one to deal with the DB creation of the tables. I didn't get too fancy just did put there the SQL commands
from `init-tables.sql` and a bit more. This is my API: 

   val db: DatabaseConfig[JdbcProfile]
   val slickSession: SlickSession
   def createTables(): Unit
   def dropTables: DBIO[Int]  
   val createProjection: DBIO[Int]
   val createSnapshot: DBIO[Int]
   val createJournal: DBIO[Int]

in here [to connect to the db](https://doc.akka.io/docs/alpakka/current/slick.html) to make [plain sql queries](https://scala-slick.org/doc/3.3.1/sql.html) and [coming from SQL to Slick](https://scala-slick.org/doc/3.3.1/sql-to-slick.html) you should find all you need.  

The other class is the `Projector.scala`. The one that takes care to read from the journal and write in the table we called `projection`

