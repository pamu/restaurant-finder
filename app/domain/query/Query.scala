package domain.query

import domain.RestaurantId

sealed trait Query

case object AllRestaurants extends Query

case class Restaurants(ids: List[RestaurantId]) extends Query