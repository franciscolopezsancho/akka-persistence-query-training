##### Adding tags
   
From the begining we just extracted the Ids from the journal. This is probably not the most useful info
you can get out of it. Let's get out the events. Everytime a command is issued if everything goes fine an
Event is created, applied to the state of the Box and if then still all is good, persisted in the journal. 
I can get a bit more complicated than this. Check [this](https://doc.akka.io/docs/akka/current/typed/persistence.html#effects-and-side-effects) out if interested. It's worth the reading
If anything fails within the whole transaction the actor will follow its supervisior decision.
By default is stop. But can be configured to be otherwise. Have a look [here](https://doc.akka.io/docs/akka/current/typed/fault-tolerance.html) if you'd like to know more.

Anyways, we wanted the events. And for that we should used same API we are now using but just call to a method
that could provide us a filter by tag. It's a good time to review what we saw in the exercise_01. A method that says
`current[X]` will just provide the events at the moment of the query and no more. It's a bounded source. I wouldn't pick that one.
Have another look at [here](https://doc.akka.io/docs/akka-persistence-jdbc/3.5.2/) to find the way to get those event.

Also I recommend not paying too much attention to the Offset idea you'll see in the above link. That will not matter at the moment.

All this is about the reading part, how to retrieve the events by tag. But the writing part also needs to be add to 
the `Box`. When issueing events the tag needs to be added. Is quite straight forward. [This](https://doc.akka.io/docs/akka/current/typed/persistence.html#tagging) should do. Just mention that when you see a `Set` of tags may not make much sense but 
I reckon it will in the next exercise. It's important to highligh that what we are looking for in here is to tag
per entity. This means we will have to include some sort of identification in the tag that will allow us later on make the query
for that specific entity.