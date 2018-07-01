package router


import controllers.{Assets, RestaurantsController}
import play.api.routing.{Router, SimpleRouter}
import play.api.routing.sird._

class RestaurantFinderRouter(restaurantController: RestaurantsController,
                             assetsController: Assets) extends SimpleRouter {
  def routes: Router.Routes = {

    case GET(p"/") => restaurantController.index

    // static resources
    case GET(p"/assets/$file*") =>
      assetsController.versioned(path = "/public", file)

    case GET(p"/$file*") =>
      assetsController.versioned(path = "/public", file)
  }

}
