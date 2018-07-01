package database.slick

import java.net.URL
import java.util.UUID

import database.driver.DatabaseDriverProvider
import shapeless._

import scala.reflect.ClassTag

trait ColumnMappers { self: DatabaseDriverProvider =>

  import self.databaseDriver.driver.api._

  implicit def longBasedMappedColumnTyped[T: ClassTag](
    implicit
    evidence: Generic.Aux[T, Long :: HNil]): BaseColumnType[T] =
    MappedColumnType.base[T, Long](
      t => evidence.to(t).head,
      f => evidence.from(f :: HNil)
    )

  implicit def stringBasedMappedColumnType[T: ClassTag](
    implicit
    evidence: Generic.Aux[T, String :: HNil]): BaseColumnType[T] =
    MappedColumnType.base[T, String](
      t => evidence.to(t).head,
      f => evidence.from(f :: HNil)
    )

  implicit def uuidBasedMappedColumnType[T: ClassTag](
    implicit
    evidence: Generic.Aux[T, UUID :: HNil]): BaseColumnType[T] =
    MappedColumnType.base[T, UUID](
      t => evidence.to(t).head,
      f => evidence.from(f :: HNil)
    )
}
