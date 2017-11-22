package me.slotbook.apistream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import me.slotbook.apistream.api.SlotbookapiStreamService
import me.slotbook.api.api.SlotbookapiService
import com.softwaremill.macwire._

class SlotbookapiStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new SlotbookapiStreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new SlotbookapiStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[SlotbookapiStreamService])
}

abstract class SlotbookapiStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[SlotbookapiStreamService](wire[SlotbookapiStreamServiceImpl])

  // Bind the SlotbookapiService client
  lazy val slotbookapiService = serviceClient.implement[SlotbookapiService]
}
