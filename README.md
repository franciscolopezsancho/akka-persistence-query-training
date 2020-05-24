# AKKA CQRS projections
The gist of this exercises is to get familiar with event sourcing and projections through [akka persistence](https://doc.akka.io/docs/akka/current/typed/persistence.html)

This assumes some familiarity with Akka Typed (at least what a Behavior is). And also a vague idea of what problem event sourcing and akka persistence try to solve.

In a nutshell we could say that after having some persisted actor in a journal. We'd like to consume those events
a form a view out of it. Generally we would use the name projection. e.g. We have multiple event on our Box entity, 
namely the elements that have been added, and we'd like to read all this event's and put them in a table so we can
query directly how many items have this Box. 

This projection have two main benefits. We are not hitting the original journal so different queries, or reades can
be done independently. And also defining the format of our projection we'll have an optimized view that align with 
an specific query with do not have to know before the fact.  

This exercises build upon some knowledge from [akka-persistence-training](https://github.com/franciscolopezsancho/akka-persistence-training/). An the code starts with essentially the same code that workshops ends with. 

Let's get started


working method:

You can go the branch you want

from the previous we assume tables have been created

01. 
   
   We should have a database the can hold the journal. This would be located in the provided docker container.
   We are providing some test that start an entity and send some commands to it. The events produced after those 
   commands will generate some entries in the journal. Without the journal test will fail.  

   Let's add now a projection. First just print it.
   latet alppakka to stream it
   a bit more of theory: A projection is a transformation from the Source that is the journal.
   And when saying source here we use the streams semantics. AS Source -> flow -> Sink. So a projection is a stream. When querying the journal we will read the data as a stream with backpressure. Here's a bit of info:  https://doc.akka.io/docs/akka/current/stream/stream-flows-and-basics.html


02. Let's now insert that in a DB. Let's add the projection in the init-table.sql
   we'll test that manually for now.
      - for this we'll use a slick session and the result from the query in Step 2 as the Source to iterate over.
         https://doc.akka.io/docs/alpakka/current/slick.html#using-a-slick-flow-or-sink
      - and before run the test let's get into the docker mysql and run the creation ot the table `my-projection`

03. let's automate the test now and refactor. 
      Will need three things. 

      A. As tests on BoxSpec we'll need to create a persistence Entity and send an Item to it
      B. As previous test in Projection Spec you'll need to read the journal and insert in db
      
      nothing new till here
      C. Read from the my_projection table as a stream to verify we have what we expected. I recommend to print it first
      https://doc.akka.io/docs/alpakka/current/slick.html#using-a-slick-source

      D. Once we see how values come from the projection use `eventually { ... result should contain ("yada yada") ...}
      You'll need mixint the test class `with Eventually` before begin able to add 
      
      Hints: be wary of the boxId you won't be able to insert it twice

      but after all we'll have to create all tables in case of deploying the app. So ...
      Let's first rerun mysql script to clean to clean everything and 

04. Let's refactor
   Take the creation from the projection out and put its own file
   Create 


05. Let's write by tag
      you may want to have a look at 
      Tags are there for you to be able to work with multiple entities. When you query the journal you may filter this
      way the entities you're processor is interested in. 

06. Now let's make the tag be useful not only to filter one type of entities. Now multiple processors should be able to consume the same persistence entity but each processor will process only specifics id. We'll need to define a function to define the 'specifics' such as := def partitionTag(persistenceId: String): String = {"box-tag" concat (hash(id) mod num-consumers)}. i.e partitionTag("123") => "box-tag-0" . 

    I would recommend first add this "num-consumers" as a hardcoded, 2 would be just fine.  Then test all this with two processors consuming each one one event. 

    Then after test passes. Let's refactor and make the num-consumers and also "box-tag" string come from application.conf.

    

07. Now let's tag by event, and filter by that. There are pretty cool things you can do with EventAdapter which
tagger is just a subset. https://doc.akka.io/docs/akka/current/typed/persistence.html#event-adapters 
