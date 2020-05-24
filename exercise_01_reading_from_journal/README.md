# AKKA CQRS projections

#### Before starting
   This workshop is a continuation of [akka-persistance-training](link...)

   In case you didn't have a look at it first I'll explain what we left off.

   We created a `Box.scala` as an Akka-persistence-entity that accepted messages of the type 'ItemAdded'. The only
   thing we were doing with that entity was sending Commands of that type and getting back Accepted or Rejected.  
   Depending of whether the Box was already full or not.

   So we'll start from there here and to test everything is fine we should be running the database that holds the events
   of the Box. Events that are saved in a table named journal. Without these test will fail. Will be enough to run `docker-compose up`
   from the docker folder and create the database and tables required.
   You can find this info in application.conf and init-tables.sql

  
#### A bit of understanding

   A projection is a transformation of the journal. In this table you store all the history of all the persistence-entities, our boxes. And to replay history to see the current state of an specific Box part of those events we'll need to be read.  
   In our case the act of projecting the events to another store is through an Akka-Stream. It quite simple, just a query made to the table that will produce a Source we can consume in a stream fashion
   
   Here's more info about streams if you feel curious:  https://doc.akka.io/docs/akka/current/stream/stream-flows-and-basics.html
   But if you understood so far then don't worry about that.
  

#### The task
   
   The idea then, is to consume events from the journal. This will be done creating a Source that comes from the appropriate akka-persistance-jdbc query you decide. Using that library you can find three kinds:
      current[X] which is a bounded stream.
      event[X] which is an unbounded stream.
      [x]Ids which is just Ids, bounded or not
   
   I would recommend to retreive the Ids. The task would be to print them in the console when running the test.
   I would care to create an specific class for this. We are just learning how to use the library so I'd just work
   on `ProjectionSpec`.

      
