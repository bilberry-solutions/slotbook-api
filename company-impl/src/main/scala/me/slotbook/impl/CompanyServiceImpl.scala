package me.slotbook.impl

import java.util.UUID

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.Materializer
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import me.slotbook.api.CompanyService
import me.slotbook.api.model.{Company, CompanyContent}

import scala.concurrent.ExecutionContext

class CompanyServiceImpl(persistentEntityRegistry: PersistentEntityRegistry, system: ActorSystem)(implicit ec: ExecutionContext, mat: Materializer)
  extends CompanyService {
  persistentEntityRegistry.register(new CompanyEntity)

  override def getCompanyById(id: UUID): ServiceCall[NotUsed, Company] = ServiceCall { _ =>
    persistentEntityRegistry.refFor[CompanyEntity](id.toString).ask(GetCompany).map {
      case Some(content) => Company(id, content.name)
      case None => throw NotFound(s"Company with id $id")
    }
  }

  override def addCompany(): ServiceCall[CompanyContent, Company] = ServiceCall { companyContent =>
    val id = UUID.randomUUID()

    persistentEntityRegistry.refFor[CompanyEntity](id.toString)
      .ask(AddCompany(companyContent)).map(_ => Company(id, companyContent.name))
  }
}
