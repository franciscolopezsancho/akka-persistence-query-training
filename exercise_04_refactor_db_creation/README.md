### Refactor 

##### TO DO:
- refactor in two classes. 
   - DBFactory
   - Projector

##### Some hints:

Now that we have an automated test I guess is time for us to clean up. What about you create a couple of classes for that?
Let's put in DBFactory the dropping and creation of the tables. In the next exercise you can see how I did it. I didn't get too fancy just put there the SQL commands from `init-tables.sql` and a bit more. This could be the API:

   val db: DatabaseConfig[JdbcProfile]
   val slickSession: SlickSession
   def createTables(): Unit
   def dropTables: DBIO[Int]  
   val createProjection: DBIO[Int]
   val createSnapshot: DBIO[Int]
   val createJournal: DBIO[Int]

Have a look in here [to connect to the db](https://doc.akka.io/docs/alpakka/current/slick.html) to make [plain sql queries](https://scala-slick.org/doc/3.3.1/sql.html) and [coming from SQL to Slick](https://scala-slick.org/doc/3.3.1/sql-to-slick.html) you should find all you need.  

The other class is the `Projector.scala`. This one takes care to read from the journal and write in the table we called `my_projection`

