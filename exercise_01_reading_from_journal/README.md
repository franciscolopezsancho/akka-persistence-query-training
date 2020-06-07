# AKKA CQRS projections

#### Starting point:

   We have a `Box.scala` as an Akka-persistence-entity that accepted messages of the type `AddItem`. The only
   thing we were doing to that entity was sending Commands of that type and getting back Accepted or Rejected.  
   Depending of whether the Box was already full or not.

  
#### A bit context before the task

   We will call projection to a transformation of the journal. In this journal it's stored all the history, the events, of all the persistence-entities, our boxes in this case. To replay history to see the current state of an specific `Box` part of those events we'll need to be read.  
   In our case the act of projecting the events to another store is through an Akka-Stream. It quite simple, just a query made to the table through the already existing API will produce a Source we can consume in a stream fashion
   
   Here's more info about streams if you feel curious:  https://doc.akka.io/docs/akka/current/stream/stream-flows-and-basics.html
   But if you understood so far then don't worry much about it.
  

##### TO DO:
   - Check that we have in place a working `Box`. This is a green on tests in `BoxSpec`
   - Spin up the docker container with a mysql database in it and create the required tables from `init-tables`
   - `ProjectionSpec` should be red but that's just a hint for you were to start.
   

##### Some hints:
The idea then, is to consume events from the journal. This will be done creating a Source that comes from the appropriate [akka-persistance-jdbc](https://doc.akka.io/docs/akka-persistence-jdbc/3.5.2/) query. Using that library you can find two kinds:
      current[X] which are bounded streams.
      and the rest like e.g. eventsByTag, which are an unbounded stream.
   
Let's start retreiving the Ids. The task would be then to print them in the console when running the test. I wouldn't care to create an specific class for this. We are just learning how to use the library so I'd just write code in the `ProjectionSpec` class.

      
