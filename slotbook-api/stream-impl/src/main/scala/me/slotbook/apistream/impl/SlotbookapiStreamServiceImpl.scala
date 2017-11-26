package me.slotbook.apistream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import me.slotbook.api.SlotbookapiService
import me.slotbook.apistream.api.SlotbookapiStreamService

import scala.concurrent.Future

/**
  * Implementation of the SlotbookapiStreamService.
  */
class SlotbookapiStreamServiceImpl(slotbookapiService: SlotbookapiService) extends SlotbookapiStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(slotbookapiService.hello(_).invoke()))
  }
}
