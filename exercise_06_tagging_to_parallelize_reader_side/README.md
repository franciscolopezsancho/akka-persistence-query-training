##### parallelize reader side

##### TO DO:
    - create a different tag depending on the BoxId and the level of parallelism desired.

        def calculateTag(paralellism: Int, entityId: String): Int

        Such as:
            assert(Box.calculateTag(2,box0Id) == 0)
            assert(Box.calculateTag(2,box1Id) == 1)

            where box0Id and box1Id are variables we just made up
            to hold different box's ids. 

        

    - create a test to prove we get two different events with a different tag

         result should contain(s"ItemAdded($box0Id,f000,2)")
         result should not contain(s"ItemAdded($box1Id,f111,2)")

         result should not contain(s"ItemAdded($box0Id,f000,2)")
         result should contain(s"ItemAdded($box1Id,f111,2)")
            

##### A bit of explanation 

Let's now store events by a different tag depending on the box's id. This will help
to create different consumers of those projections, each one reading its own tag so can parallelize reading. 

In order to do this we need to create a function to calculate some partition indicator that we will add to
our `Box` when defining the taggers. 

