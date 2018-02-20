package me.slotbook.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire._
import me.slotbook.company.api.CompanyService
import play.api.libs.ws.ahc.AhcWSComponents


class CompanyLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext): LagomApplication = {
    new CompanyApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }
  }

  override def loadDevMode(context: LagomApplicationContext) =
    new CompanyApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[CompanyService])
}

abstract class CompanyApplication(context: LagomApplicationContext) extends LagomApplication(context)
  with AhcWSComponents with CassandraPersistenceComponents {

  override lazy val lagomServer: LagomServer = serverFor[CompanyService](wire[CompanyServiceImpl])
  override lazy val jsonSerializerRegistry = CompanySerializerRegistry

  persistentEntityRegistry.register(wire[CompanyEntity])
}
