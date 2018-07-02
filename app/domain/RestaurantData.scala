package domain

case class RestaurantData(name: String,
                          phoneNumber: String,
                          cuisines: List[String],
                          address: String,
                          description: String)
