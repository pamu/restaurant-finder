package controllers

import java.util.UUID

import cats.data.Validated.{Invalid, Valid}
import common.http.result.{HttpFailure, HttpSuccess}
import common.results
import domain.{Restaurant, RestaurantData, RestaurantId}
import domain.query.{AllRestaurants, Restaurants}
import play.api.Environment
import play.api.mvc._

import scala.concurrent.ExecutionContext
import io.circe.syntax._
import play.api.libs.circe.CirceBodyParsers
import services.RestaurantsService
import common.syntax._
import codecs._
import domain.actions.{CreateRestaurant, DeleteRestaurants, UpdateAction}
import monix.execution.Scheduler.Implicits.global
import common.validations._
import monix.eval.Task

class RestaurantsController(service: RestaurantsService)(
  implicit
  val environment: Environment,
  val executionContext: ExecutionContext,
  val controllerComponents: ControllerComponents) extends BaseController with CirceBodyParsers {

  def index = Action {
    Redirect("/v1/healthcheck")
  }

  def healthCheck = Action {
    Ok(HttpSuccess("System is up and running!").asJson)
  }

  def restaurant(id: UUID) = Action.async {
    service(Restaurants(RestaurantId(id))).playResult.runAsync
  }

  def allRestaurants = Action.async {
    service(AllRestaurants).playResult.runAsync
  }

  def createRestaurant = Action.async(circe.json[RestaurantData]) { req =>
    validateRestaurantData(req.body) match {
      case Valid(a: RestaurantData) => service(CreateRestaurant(a)).playResult.runAsync
      case Invalid(e) => Task.now(BadRequest(HttpFailure(e.toList.map(_.getMessage)).asJson)).runAsync
    }
  }

  def updateRestaurant = Action.async(circe.json[UpdateAction]) { req =>
    validateUpdateAction(req.body) match {
      case Valid(a) => service(a).playResult.runAsync
      case Invalid(e) => Task.now(BadRequest(HttpFailure(e.toList.map(_.getMessage)).asJson)).runAsync
    }
  }

  def deleteRestaurant(id: UUID) = Action.async {
    service(DeleteRestaurants(RestaurantId(id))).playResult.runAsync
  }


}
