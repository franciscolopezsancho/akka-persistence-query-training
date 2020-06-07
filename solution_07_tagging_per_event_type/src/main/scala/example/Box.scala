package example

import akka.persistence.typed.scaladsl.EventSourcedBehavior
import akka.persistence.typed.scaladsl.Effect
import akka.persistence.typed.PersistenceId
import akka.actor.typed.Behavior
import akka.actor.typed.ActorRef
  
//BINDING SERIALIZER WITH THIS TRAIT
trait CborSerializable

object Box {

  //STATE
  case class Item(description: String, size: Int)
  final case class State(items: List[Item], maxCapacity: Int) {
    def addItem(item: Item): State = {
      this.copy(items = item +: items)
    }
    def roomLeft = maxCapacity - items.map(_.size).sum

    def emptyBox: State = {
      State.empty(maxCapacity)
    }
  }

  object State {
    def empty(maxCapacity: Int) = State(List.empty, maxCapacity)
  }


  //COMMANDS
  sealed trait Command
  case class AddItem(
      description: String,
      size: Int,
      replyTo: ActorRef[Confirmation]
  ) extends Command
  case class CleanBox(replyTo: ActorRef[Confirmation]) extends Command


  //EVENTS
  sealed trait Event extends CborSerializable {
    def boxId: String
  }
  case class ItemAdded(boxId: String, description: String, size: Int) extends Event
  case class BoxCleaned(boxId: String) extends Event

  //REPLIES
  sealed trait Confirmation
  case class Accepted(roomLeft: Int) extends Confirmation
  case class Rejected(item: Item, roomLeft: Int) extends Confirmation

  def calculateTag(paralellism: Int, entityId: String): Int = {
    entityId.toInt % paralellism
  }

  def apply(boxId: String, maxCapacity: Int): Behavior[Command] = {
    EventSourcedBehavior[Command, Event, State](
      PersistenceId("Box", boxId),
      State.empty(maxCapacity),
      (state, command) => commandHandler(boxId, state, command),
      (state, event) => eventHandler(state, event)
    ).withTagger{
      case event: BoxCleaned => Set("box-tag-" + calculateTag(2,boxId),"box-cleaned") // you may want just one or the other. 
      case _ =>  Set("box-tag-" + calculateTag(2,boxId))
     }
  }

  def commandHandler(boxId: String, state: State, command: Command): Effect[Event, State] = {
    command match {
      case AddItem(description, size, replyTo) => {
        if (size < state.roomLeft) {
          Effect
            .persist(ItemAdded(boxId, description, size))
            .thenRun(state => replyTo ! Accepted(state.roomLeft))
        } else {
          replyTo ! Rejected(Item(description, size), state.roomLeft)
          Effect.none
        }
      }
      case CleanBox(replyTo) => {
        Effect
            .persist(BoxCleaned(boxId))
            .thenRun(state => replyTo ! Accepted(state.roomLeft)) 
      }
    }
  }
  def eventHandler(state: State, event: Event): State = {
    event match {
      case ItemAdded(boxId, description, size) => {
        state.addItem(Item(description, size))
      }
      case BoxCleaned(boxId) => {
        state.emptyBox
      }
    }
  }

}
