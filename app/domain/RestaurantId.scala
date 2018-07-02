package domain

import java.util.UUID

import cats.Eq

case class RestaurantId(value: UUID) extends AnyVal

object RestaurantId {
  def generate: RestaurantId = RestaurantId(UUID.randomUUID())
  implicit val eq: Eq[RestaurantId] = Eq.fromUniversalEquals[RestaurantId]
}
