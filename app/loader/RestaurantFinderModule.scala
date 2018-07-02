package loader

import controllers.{AssetsComponents, RestaurantsController}
import database.driver.DatabaseDriver
import database.tables.restaurantcuisines.RestaurantCuisinesRepo
import monix.execution.Scheduler
import org.flywaydb.play.FlywayPlayComponents
import play.api._
import play.api.mvc.{ControllerComponents, EssentialFilter}
import play.filters.cors.{CORSConfig, CORSFilter}
import play.filters.gzip.{GzipFilter, GzipFilterConfig}
import play.filters.hosts.{AllowedHostsConfig, AllowedHostsFilter}
import router.RestaurantFinderRouter
import services.RestaurantsServiceImpl
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

class RestaurantFinderModule(context: ApplicationLoader.Context)
  extends BuiltInComponentsFromContext(context)
    with AssetsComponents
    with FlywayPlayComponents {

  val corsFilter = new CORSFilter(CORSConfig.fromConfiguration(configuration))
  val gzipFilter = new GzipFilter(GzipFilterConfig.fromConfiguration(configuration))

  val allowedHostsFilter = AllowedHostsFilter(
    AllowedHostsConfig.fromConfiguration(configuration),
    httpErrorHandler
  )

  def httpFilters: Seq[EssentialFilter] = Seq(
    allowedHostsFilter,
    corsFilter,
    gzipFilter
  )

  override implicit lazy val executionContext: Scheduler = monix.execution.Scheduler.Implicits.global
  implicit val implicitEnvironment: Environment = environment
  implicit val implicitControllerComponents: ControllerComponents = controllerComponents


  val databaseDriver: DatabaseDriver = new DatabaseDriver {
    val driver: JdbcProfile = slick.jdbc.H2Profile
    val database: driver.api.Database = driver.api.Database.forURL(
      s"jdbc:h2:mem:test;MODE=PostgreSQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1",
      driver = "org.h2.Driver")
  }


  val repo = new RestaurantCuisinesRepo(databaseDriver)
  val service = new RestaurantsServiceImpl(repo)
  val restaurantsController = new RestaurantsController(service)

  override val router = new RestaurantFinderRouter(restaurantsController, assets)

  applicationLifecycle.addStopHook { () =>
    for {
      _ <- Future(databaseDriver.database.close())
    } yield ()
  }
}
