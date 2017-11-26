package me.slotbook.impl

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import me.slotbook.api.{GreetingMessage, SlotbookapiService}
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

class SlotbookapiServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  private val server = ServiceTest.startServer(
    ServiceTest.defaultSetup
      .withCassandra(true)
  ) { ctx =>
    new SlotbookapiApplication(ctx) with LocalServiceLocator
  }

  val client = server.serviceClient.implement[SlotbookapiService]

  override protected def afterAll() = server.stop()

  "api service" should {

    "say hello" in {
      client.hello("Alice").invoke().map { answer =>
        answer should ===("Hello, Alice!")
      }
    }

    "allow responding with a custom message" in {
      for {
        _ <- client.useGreeting("Bob").invoke(GreetingMessage("Hi"))
        answer <- client.hello("Bob").invoke()
      } yield {
        answer should ===("Hi, Bob!")
      }
    }
  }
}
