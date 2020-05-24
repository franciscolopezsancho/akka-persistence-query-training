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


The working method is, I hope, very simple. Just open you favorite IDE in the exercise folder you'd like to work
on. There's you'll find a README.md with the task at hand and the solution can be found in the next exercise. As each
exercise start where the previous one it's supposed to finish with. The last exercise will have its solution in
the folder with the same name but with 'solution' in the name instead of 'exercise'

The exercises are the following
      read from the journal -> Events are persisted in a store and we'd like to read them
      write to a projection -> After reading those events we want to push them to another table, our 'projection'
      automate testing -> Once we see we are actually writing in our projection we will automate a test to prove that
      refactor -> Previous steps should be done in the test itself, and not suprisingly refactor is recommended to be done over working tests
      adding tags -> A tag is a way of filtering elements and previously we were also just getting ids of entities. Now we are going to get the events that the `Box` entity produces.
      adding more tags -> Finally we'll filter events not only by entity but by event types, we'll create a new one called `BoxCleaned` that we'll try to retrieve.