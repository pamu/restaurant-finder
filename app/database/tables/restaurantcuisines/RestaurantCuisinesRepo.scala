package database.tables.restaurantcuisines

import database.driver.{DatabaseDriver, DatabaseDriverProvider}
import database.slick.ColumnMappers
import database.tables.cuisines.{CuisinesTable, CuisinesTableProvider}
import database.tables.restaurants.{RestaurantsTable, RestaurantsTableProvider}
import domain.{CuisineId, Restaurant, RestaurantData, RestaurantId}
import exceptions.RestaurantIdNotFound
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

class RestaurantCuisinesRepo(override val databaseDriver: DatabaseDriver)
  extends DatabaseDriverProvider
    with ColumnMappers
    with RestaurantsTableProvider
    with CuisinesTableProvider
    with RestaurantCuisinesTableProvider {

  import databaseDriver._
  import databaseDriver.driver.api._

  private val restaurants = TableQuery[RestaurantsTable]
  private val cuisines = TableQuery[CuisinesTable]
  private val restaurantCuisines = TableQuery[RestaurantCuisinesTable]


  // public task functions below

  def saveRestaurantData(data: RestaurantData): Task[Option[Int]] = saveRestaurantDataDBIO(data).run

  def updateRestaurant(id: RestaurantId, updater: RestaurantData => RestaurantData): Task[Int] =
    updateRestaurantDBIO(id, updater).run

  def restaurantByIds(ids: RestaurantId*): Task[Seq[Restaurant]] = restaurantByIdsDBIO(Some(ids.toSeq)).run

  def allRestaurants: Task[Seq[Restaurant]] = restaurantByIdsDBIO(None).run

  def deleteRestaurants(ids: RestaurantId*): Task[Int] = deleteRestaurantsDBIO(ids: _*).run

  // DBIO functions below

  // Private functions

  private def saveRestaurantDataDBIO(data: RestaurantData): DBIO[Option[Int]] =
    (for {
      cuisineIds <- saveCuisinesDBIO(data.cuisines)
      restaurantId = RestaurantId.generate
      _ <- restaurants += RestaurantsTable.Row(
        restaurantId,
        data.name,
        data.phoneNumber,
        data.address,
        data.description)
      result <- restaurantCuisines ++= cuisineIds.map(
        RestaurantCuisinesTable.Row(restaurantId, _))
    } yield result).transactionally


  private def updateRestaurantDBIO(id: RestaurantId, updater: RestaurantData => RestaurantData): DBIO[Int] =
    (for {
      restaurant <- restaurantByIdsDBIO(Some(Seq(id))).map(_.headOption).flatMap {
        case Some(restaurant) => DBIO.successful(restaurant)
        case None => DBIO.failed(RestaurantIdNotFound(s"Restaurant id: ${id.value} is not found"))
      }
      updatedData = updater(restaurant.restaurantData)
      _ <- deleteCuisinesDBIO(restaurant.cuisines.map(_.id): _*)
      cuisineIds <- saveCuisinesDBIO(updatedData.cuisines)
      _ <- restaurantCuisines ++= cuisineIds.map(RestaurantCuisinesTable.Row(restaurant.id, _))
      result <- restaurants
        .filter(_.id === id)
        .map(row => (row.name, row.phoneNumber, row.address, row.description))
        .update((updatedData.name, updatedData.phoneNumber, updatedData.address, updatedData.description))
    } yield result).transactionally

  private def restaurantByIdsDBIO(all: Option[Seq[RestaurantId]]): DBIO[Seq[Restaurant]] =
    (all match {
      case Some(ids) => restaurants.filter(_.id.inSet(ids.toSet))
      case None => restaurants
    }).joinLeft(restaurantCuisines)
      .on(_.id === _.restaurantId)
      .joinLeft(cuisines)
      .on { case (a, b) => a._2.filter(_.cuisineId === b.id).isDefined }.result.map {
      _.map { x: ((RestaurantsTable.Row, Option[RestaurantCuisinesTable.Row]), Option[CuisinesTable.Row]) =>
        val a = x._1._1
        val b = x._2
        RestaurantsTable.toDomain(a)(_ => b.map(CuisinesTable.toDomain).toList)
      }.groupBy(_.id).mapValues(list => list.head.copy(cuisines = list.toList.flatMap(_.cuisines))).values.toSeq
    }

  private def saveCuisinesDBIO(cuisineNames: List[String]): DBIO[List[CuisineId]] = {
    val cuisineRows = cuisineNames.map(CuisinesTable.Row(CuisineId.generate, _))
    val cuisineIds = cuisineRows.map(_.id)
    (cuisines ++= cuisineRows).map(_ => cuisineIds).transactionally
  }

  private def deleteCuisinesDBIO(ids: CuisineId*): DBIO[Int] =
    (for {
      _ <- restaurantCuisines.filter(_.cuisineId.inSet(ids.toSet)).delete
      result <- cuisines.filter(_.id.inSet(ids.toSet)).delete
    } yield result).transactionally

  private def deleteRestaurantsDBIO(ids: RestaurantId*): DBIO[Int] =
    (for {
      _ <- restaurantCuisines.filter(_.restaurantId.inSet(ids.toSet)).delete
      result <- restaurants.filter(_.id.inSet(ids.toSet)).delete
    } yield result).transactionally

}