package me.slotbook.api.impl

import me.slotbook.api.api
import me.slotbook.api.api.{SlotbookapiService}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

/**
  * Implementation of the SlotbookapiService.
  */
class SlotbookapiServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends SlotbookapiService {

  override def hello(id: String) = ServiceCall { _ =>
    // Look up the slotbook-api entity for the given ID.
    val ref = persistentEntityRegistry.refFor[SlotbookapiEntity](id)

    // Ask the entity the Hello command.
    ref.ask(Hello(id))
  }

  override def useGreeting(id: String) = ServiceCall { request =>
    // Look up the slotbook-api entity for the given ID.
    val ref = persistentEntityRegistry.refFor[SlotbookapiEntity](id)

    // Tell the entity to use the greeting message specified.
    ref.ask(UseGreetingMessage(request.message))
  }


  override def greetingsTopic(): Topic[api.GreetingMessageChanged] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(SlotbookapiEvent.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(helloEvent: EventStreamElement[SlotbookapiEvent]): api.GreetingMessageChanged = {
    helloEvent.event match {
      case GreetingMessageChanged(msg) => api.GreetingMessageChanged(helloEvent.entityId, msg)
    }
  }
}
