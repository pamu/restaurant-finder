package database.driver

import slick.dbio.Effect.Write
import slick.jdbc.JdbcProfile
import monix.eval.Task

trait DatabaseDriver {

  val driver: JdbcProfile

  import driver.api._

  val database: Database

  type DBWriteAction = DBIOAction[Unit, NoStream, Write]
  lazy val noDBUpdates: DBWriteAction = DBIO.successful[Unit](())

  implicit class DBIOOps[R](a: DBIOAction[R, NoStream, Nothing]) {
    def run: Task[R] = Task.deferFuture(database.run(a))
  }

}
