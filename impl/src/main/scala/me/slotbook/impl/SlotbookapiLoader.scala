package me.slotbook.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.softwaremill.macwire._
import me.slotbook.api.SlotbookapiService

class SlotbookapiLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new SlotbookapiApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new SlotbookapiApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[SlotbookapiService])
}

abstract class SlotbookapiApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[SlotbookapiService](wire[SlotbookapiServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = SlotbookapiSerializerRegistry

  // Register the slotbook-api persistent entity
  persistentEntityRegistry.register(wire[SlotbookapiEntity])
}
