
# AKKA CQRS projections
The gist of this exercises is to get familiar with event sourcing and projections through [akka persistence query](https://doc.akka.io/docs/akka/current/persistence-query.html)

![projections](projection1.png)

This assumes some familiarity with Akka Typed (at least what a Behavior is). And also a vague idea of what problem event sourcing and akka persistence try to solve.

In a nutshell we could say that after having some persisted actor events in a journal, we'd like to consume those events and form a view out of it. This view we would call it projection.
In our use case we have multiple event from our `Box` persistence entity, namely the elements that have been added, and we'd like to read these event's and put them in a diferent table. We'll explore a three different projections out of this journal.

This projections have two main benefits. Performance and flexibility. We are not hitting the original journal so we can scale the readers without compromising the writing. And also we are free to define the format of our projection to align with an specific query with do not have to know before the fact.  

This exercises build upon some knowledge from [akka-persistence-training](https://github.com/franciscolopezsancho/akka-persistence-training/). An this code starts with essentially the same code that workshops ended with.

#### Let's get started


The working method is, I hope, very simple. Just open you favorite IDE in the exercise folder you'd like to work
on. There's you'll find a README.md with the task at hand while the solution can be found in the next exercise. As each exercise start where the previous one it's supposed to end. The last exercise will have its solution in
the folder with the same name but with 'solution' in the name instead of 'exercise'

The exercises are the following

   1. Reading from the journal : Events are persisted in a storage and we'd like to read them
   2. Write to a projection : After reading those events we want to push them to another table, our 'projection'
   3. Automate testing : Once we see we are actually writing in our projection table we will automate a test to prove that
   4. Refactor : Previous steps should be done in the test itself, and not suprisingly refactor is recommended to be done over working tests. So now let's pull out a couple of classes. A DBFactory and a Projector.
   5. Adding tags: A tag is a technique that will allow filtering elements when creating the projection. If we have event tagged we can query the journal by them while creating our projection.
   6. Adding tags to parallelize reading: We'll create different tags depending on the id f the `Box` so then when consuming from the database different reader have to consume from different tables/projections.
   7. Adding tags by event: Finally we'll filter events not only by entity but by event types, we'll create a new event called `BoxCleaned` and we'll try to consume them from the journal to create and specific projection for this type of events.