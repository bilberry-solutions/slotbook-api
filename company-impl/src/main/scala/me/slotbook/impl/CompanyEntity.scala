package me.slotbook.impl

import java.time.LocalDateTime

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import me.slotbook.company.api.model.{Company, CompanyContent}
import play.api.libs.json.{Format, Json}

import scala.collection.immutable.Seq

class CompanyEntity extends PersistentEntity {
  override type Command = CompanyCommand
  override type Event = CompanyEvent
  override type State = CompanyState

  override def behavior: Behavior = Actions().onCommand[AddCompany, Done] {
    case (AddCompany(companyContent), ctx, state) => ctx.thenPersist(CompanyChanged(companyContent)) { _ => Done }
  }.onReadOnlyCommand[GetCompany.type, Option[CompanyContent]] {
    case (GetCompany, ctx, state) => ctx.reply(state.company)
  }.onEvent {
    case (CompanyChanged(companyContent), state) => CompanyState(Some(companyContent), LocalDateTime.now.toString)
  }

  override def initialState: CompanyState = CompanyState.empty

}

// States declaration
case class CompanyState(company: Option[CompanyContent], timestamp: String)

object CompanyState {
  def empty: CompanyState = CompanyState(None, LocalDateTime.now.toString)

  implicit val jsonFormat: Format[CompanyState] = Json.format
}

// Commands declaration
sealed trait CompanyCommand

case class AddCompany(company: CompanyContent) extends CompanyCommand with ReplyType[Done]

case object GetCompany extends CompanyCommand with ReplyType[Option[CompanyContent]]

object AddCompany {
  implicit val format: Format[AddCompany] = Json.format[AddCompany]
}

// Events declaration
sealed trait CompanyEvent extends AggregateEvent[CompanyEvent] {
  def aggregateTag = CompanyEvent.Tag
}

object CompanyEvent {
  val Tag: AggregateEventTag[CompanyEvent] = AggregateEventTag[CompanyEvent]
}

case class CompanyChanged(company: CompanyContent) extends CompanyEvent

object CompanyChanged {
  implicit val format: Format[CompanyChanged] = Json.format[CompanyChanged]
}


object CompanySerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[AddCompany],
    JsonSerializer[CompanyContent],
    JsonSerializer[CompanyChanged],
    JsonSerializer[Company]
  )
}

