package database.tables.restaurants

import domain.{Cuisine, Restaurant, RestaurantId}

object RestaurantsTable {

  case class Row(id: RestaurantId,
                 name: String,
                 phoneNumber: String,
                 address: String,
                 description: String)

  def toDomain(row: Row)(f: RestaurantId => List[Cuisine]): Restaurant =
    Restaurant(
      row.id,
      row.name,
      row.phoneNumber,
      f(row.id),
      row.address,
      row.description)
}
