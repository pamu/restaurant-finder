package domain.actions

import domain.RestaurantId

sealed trait Action

case class CreateRestaurant(name: String,
                            phoneNumber: String,
                            cuisines: List[String],
                            address: String,
                            description: String) extends Action


case class UpdateRestaurant(id: RestaurantId,
                            name: String,
                            phoneNumber: String,
                            cuisines: List[String],
                            address: String,
                            description: String) extends Action

