package domain.actions

import domain.{RestaurantData, RestaurantId}

sealed trait Action
sealed abstract class UpdateAction(val id: RestaurantId) extends Action
sealed abstract class DeleteAction extends Action
case class CreateRestaurant(data: RestaurantData) extends Action
case class UpdateRestaurant(override val id: RestaurantId, data: RestaurantData) extends UpdateAction(id)
case class UpdateRestaurantName(override val id: RestaurantId, name: String) extends UpdateAction(id)
case class UpdateRestaurantPhoneNumber(override val id: RestaurantId, phoneNumber: String) extends UpdateAction(id)
case class UpdateRestaurantCuisines(override val id: RestaurantId, cuisines: List[String]) extends UpdateAction(id)
case class UpdateRestaurantAddress(override val id: RestaurantId, address: String) extends UpdateAction(id)
case class UpdateRestaurantDescription(override val id: RestaurantId, description: String) extends UpdateAction(id)
case class DeleteRestaurants(ids: RestaurantId*) extends DeleteAction
