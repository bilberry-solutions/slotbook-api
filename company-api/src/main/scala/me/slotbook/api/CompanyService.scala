package me.slotbook.api

import java.util.UUID

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import me.slotbook.api.model.{Company, CompanyContent}

trait CompanyService extends Service {

  override final def descriptor: Descriptor = {
    import Service._
    import com.lightbend.lagom.scaladsl.api.transport.Method

    named("company-service")
      .withCalls(
        restCall(Method.GET, "/companies/:companyId", getCompanyById _),
        restCall(Method.POST, "/companies", addCompany _))
        //restCall(Method.PUT, "/companies", editCompany _),
        //restCall(Method.DELETE, "/companies", deleteCompany _))
      .withAutoAcl(false)
  }

  def getCompanyById(id: UUID): ServiceCall[NotUsed, Company]

  def addCompany(): ServiceCall[CompanyContent, Company]

  //def editCompany(id: UUID): ServiceCall[CompanyContent, Company]

  //def deleteCompany(id: UUID): ServiceCall[NotUsed, NotUsed]
}
