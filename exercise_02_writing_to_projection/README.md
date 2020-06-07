### Writing to a projection

##### TO DO:

* create table
* write to that table the events from the journal
* check that's the case by getting inside the docker container and making a query to that table

##### Some hints:

Now we are going to have create a table where we want to write what we get from the journal.
That table we could call it 'projection'. 
Then we should use Slick to do the insert as we are already getting the data throught de JDBCJournal. The how-to
you can find it in [here](https://doc.akka.io/docs/alpakka/current/slick.html)


