package domain

case class Restaurant(id: RestaurantId,
                      name: String,
                      phoneNumber: String,
                      cuisines: List[Cuisine],
                      address: String,
                      description: String) {

  def restaurantData: RestaurantData = RestaurantData(
    name,
    phoneNumber,
    cuisines.map(_.name),
    address,
    description
  )

}
