package me.slotbook.company.api

import java.util.UUID

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import me.slotbook.company.api.model.{Company, CompanyContent}

trait CompanyService extends Service {

  override final def descriptor: Descriptor = {
    import Service._
    import com.lightbend.lagom.scaladsl.api.transport.Method

    named("company-service")
      .withCalls(
        restCall(Method.GET, "/companies/:companyId", getCompanyById _))
      .withAutoAcl(true)
  }

  def getCompanyById(id: UUID): ServiceCall[NotUsed, Company]
}
