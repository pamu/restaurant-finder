package database.tables.cuisines

import database.driver.DatabaseDriverProvider
import database.slick.ColumnMappers
import domain.CuisineId

trait CuisinesTableProvider {
  self: DatabaseDriverProvider
    with ColumnMappers =>

  import databaseDriver.driver.api._

  class CuisinesTable(tag: Tag) extends Table[CuisinesTable.Row](tag, "cuisines") {
    def id = column[CuisineId]("id", O.PrimaryKey)
    def name = column[String]("name")

    def * = (id, name) <> (
      (CuisinesTable.Row.apply _).tupled,
      CuisinesTable.Row.unapply)
  }

}