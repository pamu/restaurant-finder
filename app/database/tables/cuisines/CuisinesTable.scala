package database.tables.cuisines

import domain.{Cuisine, CuisineId}


object CuisinesTable {

  case class Row(id: CuisineId, name: String)

  def toDomain(row: Row): Cuisine = Cuisine(row.id, row.name)
}
