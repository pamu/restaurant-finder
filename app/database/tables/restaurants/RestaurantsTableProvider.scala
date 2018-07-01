package database.tables.restaurants

import database.driver.DatabaseDriverProvider
import database.slick.ColumnMappers
import domain.RestaurantId

trait RestaurantsTableProvider {
  self: DatabaseDriverProvider
    with ColumnMappers =>

  import databaseDriver.driver.api._

  class RestaurantsTable(tag: Tag) extends Table[RestaurantsTable.Row](tag, "restaurants") {
    def id = column[RestaurantId]("id", O.PrimaryKey)
    def name = column[String]("name")
    def phoneNumber = column[String]("phone_number")
    def address = column[String]("address")
    def description = column[String]("description")

    def * = (id, name, phoneNumber, address, description).mapTo[RestaurantsTable.Row]
  }

}
