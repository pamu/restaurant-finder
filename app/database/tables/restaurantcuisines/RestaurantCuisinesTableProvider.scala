package database.tables.restaurantcuisines

import database.driver.DatabaseDriverProvider
import database.slick.ColumnMappers
import database.tables.cuisines.CuisinesTableProvider
import database.tables.restaurants.RestaurantsTableProvider
import domain.{CuisineId, RestaurantCuisineId, RestaurantId}

trait RestaurantCuisinesTableProvider {
  self: DatabaseDriverProvider
    with ColumnMappers
    with RestaurantsTableProvider
    with CuisinesTableProvider =>

  import databaseDriver.driver.api._

  class RestaurantCuisinesTable(tag: Tag) extends Table[RestaurantCuisinesTable.Row](tag, "restaurant_cuisines") {
    def id = column[RestaurantCuisineId]("id", O.PrimaryKey, O.AutoInc)
    def restaurantId = column[RestaurantId]("restaurant_id")
    def cuisineId = column[CuisineId]("cuisine_id")

    def restaurantIdFK = foreignKey(
      "restaurant_cuisines_restaurant_id_fk",
      restaurantId,
      TableQuery[RestaurantsTable]
    )(_.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade)

    def cuisineIdFK = foreignKey(
      "restaurant_cuisines_cuisine_id_dk",
      cuisineId,
      TableQuery[CuisinesTable]
    )(_.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade)

    def * = (restaurantId, cuisineId, id.?).mapTo[RestaurantCuisinesTable.Row]
  }

}


object RestaurantCuisinesTable {

  case class Row(restaurantId: RestaurantId, cuisineId: CuisineId, id: Option[RestaurantCuisineId] = None)

}
