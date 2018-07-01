package domain

case class Restaurant(id: RestaurantId,
                      name: String,
                      phoneNumber: String,
                      cuisines: List[Cuisine],
                      address: String,
                      description: String)
