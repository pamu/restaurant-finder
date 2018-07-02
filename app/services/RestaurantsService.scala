package services

import common.results.Result
import database.tables.restaurantcuisines.RestaurantCuisinesRepo
import domain.Restaurant
import domain.actions._
import domain.query.{AllRestaurants, Query, Restaurants}

trait RestaurantsService {
  def apply(action: Action): Result[String]
  def apply(query: Query): Result[Seq[Restaurant]]
}

class RestaurantsServiceImpl(repo: RestaurantCuisinesRepo) extends RestaurantsService {
  override def apply(action: Action): Result[String] = action match {
    case CreateRestaurant(data) => repo.saveRestaurantData(data).map(_ => "created")
    case updateAction: UpdateAction => updateAction match {
      case UpdateRestaurant(id, data) => repo.updateRestaurant(id, _ => data).map(_ => "updated")
      case UpdateRestaurantName(id, name) => repo.updateRestaurant(id, _.copy(name = name)).map(_ => "updated name")
      case UpdateRestaurantPhoneNumber(id, phoneNumber) =>
        repo.updateRestaurant(id, _.copy(phoneNumber = phoneNumber)).map(_ => "updated phone number")
      case UpdateRestaurantCuisines(id, cuisines) =>
        repo.updateRestaurant(id, _.copy(cuisines = cuisines)).map(_ => "updated cuisines")
      case UpdateRestaurantAddress(id, address) =>
        repo.updateRestaurant(id, _.copy(address = address)).map(_ => "updated address")
      case UpdateRestaurantDescription(id, description) =>
        repo.updateRestaurant(id, _.copy(description = description)).map(_ => "updated description")
    }
    case DeleteRestaurants(ids) => repo.deleteRestaurants(ids).map(_ => "deleted")
  }
  override def apply(query: Query): Result[Seq[Restaurant]] = query match {
    case AllRestaurants => repo.allRestaurants
    case Restaurants(ids) => repo.restaurantByIds(ids)
  }
}
