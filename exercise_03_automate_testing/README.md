##### Automate testing

##### TO DO:
    - (not to do) instead of manually go to the DB to check we are inserting
    - create a test to check that's the case. Such as when reading from `my_projection` then `result should contain(s"Box|$boxId")`

##### Some hints:

Now we should add some automation to the test we have. In order to do that now we have to read from the table `my_projection` and check that the event we had in the journal made it all the way through there. Again we can use Slick to consume from `my_projection`. Make sure that table exists before start so all tests but one are green at the beginning. There's a failing test just to point out where to start.

you should find all you need in the same [link](https://doc.akka.io/docs/alpakka/current/slick.html) you used in the previous exercise.


