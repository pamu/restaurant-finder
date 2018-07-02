package router


import java.util.UUID

import controllers.{Assets, RestaurantsController}
import play.api.routing.{Router, SimpleRouter}
import play.api.routing.sird._

class RestaurantFinderRouter(restaurantController: RestaurantsController,
                             assetsController: Assets) extends SimpleRouter {
  def routes: Router.Routes = {

    case GET(p"/") => restaurantController.index

    case GET(p"/v1/healthcheck") => restaurantController.healthCheck

    case GET(p"/restaurants/${uuid(id)}") => restaurantController.restaurant(id)

    case GET(p"/restaurants") => restaurantController.allRestaurants

    case POST(p"/restaurants") => restaurantController.createRestaurant

    case PUT(p"/restaurants") => restaurantController.updateRestaurant

    case DELETE(p"/restaurants/${uuid(id)}") => restaurantController.deleteRestaurant(id)

    // static resources
    case GET(p"/assets/$file*") =>
      assetsController.versioned(path = "/public", file)

    case GET(p"/$file*") =>
      assetsController.versioned(path = "/public", file)
  }

  val uuid = new PathBindableExtractor[UUID]
}
