package me.slotbook.company.api.model

import java.util.UUID

import play.api.libs.json.{Format, Json}

case class Company(id: UUID, name: String)

object Company {
  implicit def format: Format[Company] = Json.format[Company]
}

case class CompanyContent(name: String)

object CompanyContent {
  implicit val format: Format[CompanyContent] = Json.format[CompanyContent]
}