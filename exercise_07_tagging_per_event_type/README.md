### Tagging per event type

##### TO DO:

* create a new Command and its event. `CleanBox` -> `BoxCleaned`
* create a projection only for these new events
* create a test to prove we get only those events in our projection table. Such as:
            

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