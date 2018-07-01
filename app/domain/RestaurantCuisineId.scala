package domain

import java.util.UUID

import cats.Eq

// Auto generated id
case class RestaurantCuisineId(id: Long) extends AnyVal

object RestaurantCuisineId {
  implicit val eq: Eq[RestaurantCuisineId] = Eq.fromUniversalEquals[RestaurantCuisineId]
}