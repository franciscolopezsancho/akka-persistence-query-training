# AKKA CQRS projections



##### Creating the projection. Writing to DB. 

    Now we are going to have create a table were we want to write what we get from the journarl. 
    That table we could call it 'projection'. And the idea would be to use Slick to do the insert. The how-to
    you can find [here](https://doc.akka.io/docs/alpakka/current/slick.html)


