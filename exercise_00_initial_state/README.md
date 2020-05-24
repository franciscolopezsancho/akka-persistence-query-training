# AKKA CQRS projections

Let's get started

from the previous we assume tables have been created

1. We should have a database the can hold the journal. This would be located in the provided docker container.
   We are providing some test that start an entity and send some commands to it. The events produced after those 
   commands will generate some entries in the journal. Without the journal test will fail.  

   In here we should start the storage that we can see we have in application.conf. Will be enough to run `docker-compose up`
   From the docker folder and create the database required you can find in the configuration. Once that's done create the necesary tables
   and then check run sbt "exercise_00_initial_state test"

   please be aware at this stage we are dependent on the state of the database. Running this tests multiple times could make it fail.