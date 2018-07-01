package services

import database.tables.restaurantcuisines.RestaurantCuisinesRepo
import domain.Restaurant
import domain.actions.Action
import domain.query.Query
import monix.eval.Task

trait RestaurantService {
  def apply(action: Action): Task[Unit]
  def apply(query: Query): Task[List[Restaurant]]
}

class RestaurantServiceImpl(restaurantCuisinesRepo: RestaurantCuisinesRepo) extends RestaurantService {
  override def apply(action: Action): Task[Unit] = ???

  override def apply(query: Query): Task[List[Restaurant]] = ???
}
