package controllers

import play.api.Environment
import play.api.mvc._

import scala.concurrent.ExecutionContext

class RestaurantsController()(
  implicit
  val environment: Environment,
  val executionContext: ExecutionContext,
  val controllerComponents: ControllerComponents) extends BaseController {

  def index = Action {
    Ok("hello!")
  }

}
