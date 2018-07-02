package domain

import java.util.UUID

import cats.Eq

case class CuisineId(value: UUID) extends AnyVal

object CuisineId {
  implicit val eq: Eq[CuisineId] = Eq.fromUniversalEquals[CuisineId]

  def generate: CuisineId = CuisineId(UUID.randomUUID())
}
