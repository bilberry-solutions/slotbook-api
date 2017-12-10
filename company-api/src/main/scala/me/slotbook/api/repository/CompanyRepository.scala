package me.slotbook.api.repository

import me.slotbook.api.model.Company

import scala.concurrent.Future

/**
  * Trait describes the data access methods applicable to Company.
  */
trait CompanyRepository {
  def findById(companyId: Int): Future[Company]
}
