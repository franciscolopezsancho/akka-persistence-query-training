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

from the previous we assume tables have been created


2. Let's add now a projection. First just print it.
   
   a bit more of theory: A projection is a transformation from the Source that is the journal.
   And when saying source here we use the streams semantics. AS Source -> flow -> Sink. So a projection is a stream. When querying the journal we will read the data as a stream. Here's a bit of info:  https://doc.akka.io/docs/akka/current/stream/stream-flows-and-basics.html
   You'll basically need to things:
   The jdbc connector to our mysql you will find in : https://doc.akka.io/docs/alpakka/current/index.html
   And the akka-persistence-jdbc to which implements an API to consume from a JDBCJournal. All you need to know is here https://doc.akka.io/docs/akka-persistence-jdbc/current/ . 

   The idea here is to consume events from the journal. This will be done createing a Source that comes from the appropriate akka-persistance-jdbc query you decide. I would recommend to retreive the Ids. It's all done for you though. You just need to pick
   which method from the library you want to use depending on what you want to retrieve.
### Tagging per event type

##### TO DO:
    - create a new Command and its event. `CleanBox` -> `BoxCleaned`
    - create a projection only for these new events
    - create a test to prove we get only those events in our projection table. Such as:
            

##### Some more explanation 

Now that we are getting events by tag, there's an use case will probably happen more often than not.
You'll need to filter by certain events. This is, instead of all the events of an entity you want
all the events of all the entities as long they are an specific event. When you understand how this
works you'll see you can mix and match all your filtering needs with this two means to tag.

You already have all the info you need. Keep looking at the same very same API you already used in the previous exercise. There's all the info you need but code wise we'll need something else. A new event, so we can filter by it. Till now the only event could
get out of the `AddItem` is `ItemAdded` but if we want to filter for specific events we should add at least one more.
I would recommend to add a command `CleanBox` that will remove all the items from the Box and produce a `BoxCleaned` event
that as any other will be persisted in the journal. 

With this new type of events `BoxCleaned`, let's make a projection. Avoiding any other type, in our case `ItemAdded`.
So let write this down and make a test that proves that we are receiving just `BoxCleaned` events in our projection table.