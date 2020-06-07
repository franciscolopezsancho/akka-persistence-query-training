### Adding tags

##### TO DO:

* Make sure you add a tag when persist events
* Read form the journal events by tag, not just entities Ids as we are currently doing. So you
should be checking that you get back `ItemAdded` events from the projection. 
              `result should contain(s"ItemAdded($boxId,fooo,2)")`

##### Some insights:
   
From the begining we just extracted the Ids from the journal. This is probably not the most useful info
you can get out of it. Let's get out the events now. Every time a command is issued if everything goes fine an
Event is created, applied to the state of the Box and if then still all is good, persisted in the journal. 
It can get a bit more complicated than this. Check [this](https://doc.akka.io/docs/akka/current/typed/persistence.html#effects-and-side-effects) out if interested. It's worth the reading
If anything fails within the whole transaction the actor will follow its supervisor decision.
By default it stops. But can be configured to be otherwise. Have a look [here](https://doc.akka.io/docs/akka/current/typed/fault-tolerance.html) if you'd like to know more.

Anyways, we wanted the events from the `Box`. And for that we should used same API we are now using some method
that could provide us a filter by tag. This the tag is a String we add to the event when they get persisted. It's a good time to review what we saw in the exercise_01. A method that says
`currentEventsByTag` will just provide the events at the moment of the query and no more. It's a bounded source. I wouldn't pick that one.
Have another look at [here](https://doc.akka.io/docs/akka-persistence-jdbc/3.5.2/) to find the way to get those events. And what the differences are. 

Also I recommend not paying too much attention to the Offset idea you'll see in the above link. That will not matter at the moment.
Bare in mind though that when retrieving elements by tag we are filtering events that correspond to just our `Box`. Because we just have
one kind of entity we may not realise we are filtering. But if we had other applications using the same storage we would find different 
types of events. Then it would be more obvious the need of filtering by entity type. 

All this is about the reading part, how to retrieve the events by tag. But the writing part also needs to be added to 
the `Box`. When issueing events the tag needs to be added. Is quite straight forward. [This](https://doc.akka.io/docs/akka/current/typed/persistence.html#tagging) should do. Just mention that when you see a `Set` of tags may not make much sense but 
I reckon it will in the next exercise. It's important to highligh that what we are looking for in here is to tag
per entity. This means we will have to include some sort of identification in the tag that will allow us later on make the query
for that specific entity.
