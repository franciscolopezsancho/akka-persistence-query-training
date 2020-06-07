package example


import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import org.scalatest.wordspec.AnyWordSpecLike
import Box._
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

class ProjectionSpec extends ScalaTestWithActorTestKit(ConfigFactory.load()) with AnyWordSpecLike {

  val logger = LoggerFactory.getLogger(this.getClass.getName)
  
  "A projection" should {
    "be able to read from the journal and print it" in {
      //TODO develop here all you may need to connect to the journal and print
      assert(false) 
    }
  }
    
  

  
}
