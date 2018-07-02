package restaurantfinder

import akka.stream.Materializer
import controllers.RestaurantsController
import org.scalatestplus.play.PlaySpec
import play.api.Environment
import play.api.mvc.ControllerComponents
import play.api.test.FakeRequest
import play.api.test.Helpers._
import providers.RestaurantFinderPerSuiteProvider
import codecs._
import common.http.result.HttpSuccess
import domain.{Restaurant, RestaurantData}
import io.circe.{Printer, parser}
import io.circe.syntax._
import domain.actions._
import org.scalatest.GivenWhenThen

import scala.util.Try


class RestaurantsControllerSpec extends PlaySpec
  with RestaurantFinderPerSuiteProvider
  with GivenWhenThen {

  "RestaurantsControllerSpec" must {

    "create restaurant correctly" in {

      Given("restaurant data")
      val restaurantData = RestaurantData(
        "FooBarRiga",
        "79297392732",
        List(
          "Cuisine1",
          "Cuisine2",
          "Cuisine3"
        ),
        "Riga, Latvia",
        "Foobar is best in town with lot of cuisines"
      )

      When("request post request is sent")
      val req = FakeRequest(POST, "/restaurants")
        .withBody[String](restaurantData.asJson.pretty(Printer.noSpaces))
        .withHeaders("Content-type" -> "application/json; utf-8")
      val result = route(app, req)

      Then("result status must be 200")
      result.map(status) mustBe Some(OK)

      Then("result string must be created")
      parser.decode[HttpSuccess[String]](result.map(contentAsString).get).toOption.get.result mustBe "created"
    }

    "create another restaurant correctly" in {
      Given("restaurant data")
      val restaurantData = RestaurantData(
        "FooBarBerlin",
        "997933838",
        List(
          "Cuisine4",
          "Cuisine5",
          "Cuisine6",
          "Cuisine7"
        ),
        "Berlin, Germany",
        "Foobar is best in town with hundreds of cuisines"
      )

      When("request post request is sent")
      val req = FakeRequest(POST, "/restaurants")
        .withBody[String](restaurantData.asJson.pretty(Printer.noSpaces))
        .withHeaders("Content-type" -> "application/json; utf-8")
      val result = route(app, req)

      Then("result status must be 200")
      result.map(status) mustBe Some(OK)
    }


    "list all restaurants" in {
      When("request get request is sent")
      val req = FakeRequest(GET, "/restaurants")
        .withHeaders("Content-type" -> "application/json; utf-8")
      val result = route(app, req)

      Then("result status must be 200")
      result.map(status) mustBe Some(OK)

      val restaurants = parser.decode[HttpSuccess[List[Restaurant]]](
        result.map(contentAsString).get).toOption.get.result

      restaurants.map(_.name) must contain theSameElementsAs List("FooBarRiga", "FooBarBerlin")
    }

    "update the restaurant phone number correctly" in {

      val restaurantId = restaurantByName("FooBarRiga").id

      Given("new phone number for FooBarRiga")
      val updatePhoneNumber = UpdateRestaurantPhoneNumber(restaurantId, "12345678")

      val updated = updatedRestaurant(updatePhoneNumber)

      updated.phoneNumber mustBe "12345678"
    }

    "update the restaurant name correctly" in {

      val restaurantId = restaurantByName("FooBarRiga").id

      Given("new name for FooBarRiga")
      val action = UpdateRestaurantName(restaurantId, "FooBarRigaLatvia")
      val updated = updatedRestaurant(action)

      updated.name mustBe "FooBarRigaLatvia"
    }


    "update the restaurant address correctly" in {

      val restaurantId = restaurantByName("FooBarRigaLatvia").id

      Given("new address for FooBarRigaLatvia")
      val action = UpdateRestaurantAddress(restaurantId, "Cesu iela 12, Riga, Latvia, Earth")

      val updated = updatedRestaurant(action)

      updated.address mustBe "Cesu iela 12, Riga, Latvia, Earth"
    }


    "update the restaurant description correctly" in {

      val restaurantId = restaurantByName("FooBarRigaLatvia").id

      Given("new description for FooBarRigaLatvia")
      val action = UpdateRestaurantDescription(restaurantId, "super duper restaurant")

      val updated = updatedRestaurant(action)

      updated.description mustBe "super duper restaurant"
    }

    "update the restaurant cuisines correctly" in {

      val restaurantId = restaurantByName("FooBarRigaLatvia").id

      Given("new cuisines for FooBarRigaLatvia")
      val action = UpdateRestaurantCuisines(restaurantId, List(
        "java",
        "haskell",
        "scala"
      ))

      val updated = updatedRestaurant(action)

      updated.cuisines.map(_.name) must contain theSameElementsAs List("java", "haskell", "scala")
    }


    "delete restaurant successfully" in {

      val restaurantName = "FooBarRigaLatvia"

      val restaurantId = restaurantByName(restaurantName).id

      val req = FakeRequest(DELETE, s"/restaurants/${restaurantId.value}")
        .withHeaders("Content-type" -> "application/json; utf-8")

      val result = route(app, req)

      When("result status must be 200")
      result.map(status) mustBe Some(OK)

      parser.decode[HttpSuccess[String]](
        result.map(contentAsString).get).toOption.get.result  mustBe "deleted"

      Try(restaurantByName(restaurantName)).isFailure mustBe true
    }

    "update all restaurant data correctly" in {
      val restaurantName = "FooBarBerlin"

      val restaurantId = restaurantByName(restaurantName).id

      val action = UpdateRestaurant(
        restaurantId,
        RestaurantData(
          "barfoo",
          "919191",
          List("hello", "world"),
          "fake address",
          "fake description"
        )
      )

      val restaurantData = updatedRestaurant(action)

      restaurantData.name mustBe "barfoo"
      restaurantData.phoneNumber  mustBe "919191"
      restaurantData.cuisines.map(_.name) must contain theSameElementsAs List("hello", "world")
      restaurantData.address mustBe "fake address"
      restaurantData.description mustBe "fake description"
    }

  }


  def restaurantByName(name: String): Restaurant = restaurantBy(_.name === name)

  def restaurantBy(f: Restaurant => Boolean): Restaurant = {
    When("request get request is sent")
    val req = FakeRequest(GET, "/restaurants")
      .withHeaders("Content-type" -> "application/json; utf-8")
    val result = route(app, req)
    Then("result status must be 200")
    result.map(status) mustBe Some(OK)
    val restaurants = parser.decode[HttpSuccess[List[Restaurant]]](
      result.map(contentAsString).get).toOption.get.result
    restaurants.find(f).get
  }

  def updatedRestaurant(action: UpdateAction): Restaurant = {
    Given(s"update action $action")
    And("put request is sent")
    val req = FakeRequest(PUT, "/restaurants")
      .withHeaders("Content-type" -> "application/json; utf-8")
      .withBody(action.asJson.pretty(Printer.noSpaces))

    val updatedResult = route(app, req)

    When("put result status must be 200")
    updatedResult.map(status) mustBe Some(OK)

    Then("changes must match updated values")
    restaurantBy(_.id === action.id)
  }

  implicit val environment: Environment = context.environment
  implicit val implicitControllerComponents: ControllerComponents = components.controllerComponents
  implicit val mat: Materializer = components.materializer

  val controller: RestaurantsController = components.restaurantsController

}
