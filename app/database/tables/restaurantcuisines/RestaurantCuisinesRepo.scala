package database.tables.restaurantcuisines

import database.driver.{DatabaseDriver, DatabaseDriverProvider}
import database.slick.ColumnMappers
import database.tables.cuisines.{CuisinesTable, CuisinesTableProvider}
import database.tables.restaurants.{RestaurantsTable, RestaurantsTableProvider}
import domain.{CuisineId, RestaurantId}
import monix.execution.Scheduler.Implicits.global

class RestaurantCuisinesRepo(override val databaseDriver: DatabaseDriver)
  extends DatabaseDriverProvider
    with ColumnMappers
    with RestaurantsTableProvider
    with CuisinesTableProvider
    with RestaurantCuisinesTableProvider {

  import databaseDriver.driver.api._

  private val restaurants = TableQuery[RestaurantsTable]
  private val cuisines = TableQuery[CuisinesTable]
  private val restaurantCuisines = TableQuery[RestaurantCuisinesTable]

  def saveCuisines(cuisineNames: List[String]) =
    cuisines ++= cuisineNames.map(CuisinesTable.Row(CuisineId.generate, _))

  def saveRestaurantRow(row: RestaurantsTable.Row) = restaurants += row

  def saveRestaurantCuisines(rows: List[RestaurantCuisinesTable.Row]) = restaurantCuisines ++= rows

  def restaurantById(id: RestaurantId) =
    restaurants
      .filter(_.id === id)
      .joinLeft(restaurantCuisines)
      .on(_.id === _.restaurantId)
      .joinLeft(cuisines)
      .on { case (a, b) => a._2.filter(_.cuisineId === b.id).isDefined }.result.headOption.map {
      _.map { x: ((RestaurantsTable.Row, Option[RestaurantCuisinesTable.Row]), Option[CuisinesTable.Row]) =>
        val a = x._1._1
        val b = x._2
        RestaurantsTable.toDomain(a)(_ => b.map(CuisinesTable.toDomain).toList)
      }
    }

  def allRestaurants =
    restaurants
      .joinLeft(restaurantCuisines)
      .on(_.id === _.restaurantId)
      .joinLeft(cuisines)
      .on { case (a, b) => a._2.filter(_.cuisineId === b.id).isDefined }.result.map {
      _.map { x: ((RestaurantsTable.Row, Option[RestaurantCuisinesTable.Row]), Option[CuisinesTable.Row]) =>
        val a = x._1._1
        val b = x._2
        RestaurantsTable.toDomain(a)(_ => b.map(CuisinesTable.toDomain).toList)
      }
    }

}