### Writing to a projection

##### TO DO:

* create `my_projection` table
* write to that table the events from the journal
* check that's the case by getting inside the docker container and making a query to that table

##### Some hints:

Then we should use Slick to do the insert as we are already getting the data throught de JDBCJournal. The how-to
you can find it in [here](https://doc.akka.io/docs/alpakka/current/slick.html)


