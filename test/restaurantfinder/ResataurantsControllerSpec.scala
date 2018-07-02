package restaurantfinder

import akka.stream.Materializer
import akka.util.Timeout
import controllers.RestaurantsController
import org.scalatestplus.play.PlaySpec
import play.api.Environment
import play.api.mvc.ControllerComponents
import play.api.test.FakeRequest
import play.api.test.Helpers.{POST, route}
import providers.RestaurantFinderPerSuiteProvider

import scala.concurrent.duration._

class ResataurantsControllerSpec extends PlaySpec with RestaurantFinderPerSuiteProvider {

  "AuthController" must {

    "add integers properly." in {
      1 + 2 mustBe 3
    }

    "reject bad email" in {
      implicit val timeout: Timeout = Timeout(10 seconds)
      val req = FakeRequest(POST, "/login")
        .withBody[String]("""{"email": "foo", "password": "password211"}""")
        .withHeaders("Content-type" -> "application/json; utf-8")
      val result = route(app, req)
      result.map(status) mustBe Some(BAD_REQUEST)
      println(s"${contentAsString(result.get)}")
      result.map(contentAsString).get.contains("malformed") mustBe true
    }
  }

  implicit val environment: Environment = context.environment
  implicit val implicitControllerComponents: ControllerComponents = components.controllerComponents
  implicit val mat: Materializer = components.materializer

  val controller: RestaurantsController = components.restaurantsController

}
