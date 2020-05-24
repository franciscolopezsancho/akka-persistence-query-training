
###### Refactor all the things

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

in here [plain sql queries](https://scala-slick.org/doc/3.3.1/sql.html) and [coming from SQL to Slick](https://scala-slick.org/doc/3.3.1/sql-to-slick.html) you should find all you need. 

//TODO what about the session??? don't find it anywhere

