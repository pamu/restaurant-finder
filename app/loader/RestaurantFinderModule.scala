package loader

import com.typesafe.config.ConfigFactory
import controllers.{AssetsComponents, RestaurantsController}
import database.driver.DatabaseDriver
import monix.execution.Scheduler
import org.flywaydb.play.FlywayPlayComponents
import play.api.Mode.{Dev, Prod, Test}
import play.api._
import play.api.libs.ws.ahc.AhcWSClient
import play.api.mvc.{ControllerComponents, EssentialFilter}
import play.filters.cors.{CORSConfig, CORSFilter}
import play.filters.gzip.{GzipFilter, GzipFilterConfig}
import play.filters.hosts.{AllowedHostsConfig, AllowedHostsFilter}
import router.RestaurantFinderRouter
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

class RestaurantFinderModule(context: ApplicationLoader.Context)
  extends BuiltInComponentsFromContext(context)
    with AssetsComponents
    with FlywayPlayComponents {


  override def configuration: Configuration = {
    val confFile = environment.mode match {
      case Dev => "dev.conf"
      case Test => "test.conf"
      case Prod => "prod.conf"
    }
    val environmentConfig = ConfigFactory.load(s"/environments/$confFile")
    super.configuration ++ Configuration(environmentConfig)
  }

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

  /**
    * First db driver has to load for everything to function
    */
  val databaseDriver: DatabaseDriver = environment.mode match {
    case Prod =>
      new DatabaseDriver {
        val driver: JdbcProfile = slick.jdbc.PostgresProfile
        val database: driver.api.Database =
          driver.api.Database.forConfig("db.default", configuration.underlying)
      }
    case Dev =>
      new DatabaseDriver {
        val driver: JdbcProfile = slick.jdbc.PostgresProfile
        val database: driver.api.Database =
          driver.api.Database.forConfig("db.default", configuration.underlying)
      }
    case Test =>
      new DatabaseDriver {
        val driver: JdbcProfile = slick.jdbc.H2Profile
        val database: driver.api.Database = driver.api.Database.forURL(
          s"jdbc:h2:mem:test;MODE=PostgreSQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1",
          driver = "org.h2.Driver")
      }
  }


  flywayPlayInitializer

  val wsClient = AhcWSClient()

  val homeController = new RestaurantsController()

  override val router = new RestaurantFinderRouter(homeController, assets)

  applicationLifecycle.addStopHook { () =>
    for {
      _ <- Future(wsClient.close())
      _ <- Future(databaseDriver.database.close())
    } yield ()
  }
}
