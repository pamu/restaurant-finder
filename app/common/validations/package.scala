package common

import common.results.Validation
import cats.implicits._
import domain.RestaurantData
import exceptions.{EmptyString, ValidationException}
import io.circe.{Json, Printer}
import io.circe.parser._
import codecs._
import domain.actions._

package object validations {

  def nonEmpty(targetName: String, targetValue: String): Validation[String] =
    if (targetValue.trim.nonEmpty) targetValue.validNel
    else EmptyString(s"$targetName must be non empty string. Found empty string").invalidNel

  def validateRestaurantData(data: RestaurantData): Validation[RestaurantData] =
    (nonEmpty("Name", data.name),
      nonEmpty("Phone number", data.phoneNumber),
      data.cuisines.validNel[ValidationException],
      nonEmpty("Address", data.address),
      nonEmpty("Description", data.description)
    ).mapN(RestaurantData)

  def validateUpdateAction(action: UpdateAction): Validation[UpdateAction] = action match {
    case UpdateRestaurant(_, data) => validateRestaurantData(data).map(_ => action)
    case UpdateRestaurantName(_, name) =>  nonEmpty("Name", name).map(_ => action)
    case UpdateRestaurantPhoneNumber(_, phoneNumber) => nonEmpty("Phone number", phoneNumber).map(_ => action)
    case UpdateRestaurantCuisines(_, cuisines) => cuisines.validNel[ValidationException].map(_ => action)
    case UpdateRestaurantAddress(_, address) =>  nonEmpty("Address", address).map(_ => action)
    case UpdateRestaurantDescription(_, description) => nonEmpty("Description", description).map(_ => action)
  }
}
