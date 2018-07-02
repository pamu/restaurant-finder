package domain.actions

import domain.{RestaurantData, RestaurantId}

sealed trait Action
sealed abstract class UpdateAction extends Action
sealed abstract class DeleteAction extends Action
case class CreateRestaurant(data: RestaurantData) extends Action
case class UpdateRestaurant(id: RestaurantId, data: RestaurantData) extends UpdateAction
case class UpdateRestaurantName(id: RestaurantId, name: String) extends UpdateAction
case class UpdateRestaurantPhoneNumber(id: RestaurantId, phoneNumber: String) extends UpdateAction
case class UpdateRestaurantCuisines(id: RestaurantId, cuisines: List[String]) extends UpdateAction
case class UpdateRestaurantAddress(id: RestaurantId, address: String) extends UpdateAction
case class UpdateRestaurantDescription(id: RestaurantId, description: String) extends UpdateAction
case class DeleteRestaurants(ids: RestaurantId*) extends DeleteAction
