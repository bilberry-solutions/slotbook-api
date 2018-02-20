package me.slotbook.company.impl

import java.util.UUID

import akka.actor.ActorSystem
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.testkit.PersistentEntityTestDriver
import me.slotbook.company.api.model.{Company, CompanyContent}
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.Await
import scala.concurrent.duration._

class CompanyEntitySpec extends WordSpecLike with Matchers with BeforeAndAfterAll with TypeCheckedTripleEquals {
  val actorSystem = ActorSystem("CompanyEntitySpec",
    JsonSerializerRegistry.actorSystemSetupFor(CompanySerializerRegistry))

  override def afterAll(): Unit = {
    Await.ready(actorSystem.terminate(), 10.seconds)
  }

  "Company entity" must {
    "handle AddCompanyCommand" in {
      val driver =
        new PersistentEntityTestDriver[CompanyCommand, CompanyEvent, CompanyState](actorSystem,
          new CompanyEntity, "company-1")

      val content = CompanyContent(name = "new company")
      val outcome = driver.run(AddCompanyCommand(content))
      outcome.events should === (List(CompanyChanged(content)))
    }
  }
}
