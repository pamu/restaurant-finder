import common.http.result.{HttpFailure, HttpSuccess}
import domain.actions._
import domain._
import io.circe._
import io.circe.generic.semiauto._
import domain.query._
import io.circe.syntax._
import cats.syntax.functor._

package object codecs {
  implicit lazy val cuisineIdEncoder: Encoder[CuisineId] = deriveEncoder[CuisineId]
  implicit lazy val cuisineIdDecoder: Decoder[CuisineId] = deriveDecoder[CuisineId]

  implicit lazy val restaurantIdEncoder: Encoder[RestaurantId] = deriveEncoder[RestaurantId]
  implicit lazy val restaurantIdDecoder: Decoder[RestaurantId] = deriveDecoder[RestaurantId]

  implicit lazy val cuisineEncoder: Encoder[Cuisine] = deriveEncoder[Cuisine]
  implicit lazy val cuisineDecoder: Decoder[Cuisine] = deriveDecoder[Cuisine]

  implicit lazy val restaurantEncoder: Encoder[Restaurant] = deriveEncoder[Restaurant]
  implicit lazy val restaurantDecoder: Decoder[Restaurant] = deriveDecoder[Restaurant]

  implicit lazy val restaurantDataEncoder: Encoder[RestaurantData] = deriveEncoder[RestaurantData]
  implicit lazy val restaurantDataDecoder: Decoder[RestaurantData] = deriveDecoder[RestaurantData]

  implicit lazy val createRestaurantEncoder: Encoder[CreateRestaurant] = deriveEncoder[CreateRestaurant]
  implicit lazy val createRestaurantDecoder: Decoder[CreateRestaurant] = deriveDecoder[CreateRestaurant]

  implicit lazy val updateRestaurantEncoder: Encoder[UpdateRestaurant] = deriveEncoder[UpdateRestaurant]
  implicit lazy val updateRestaurantDecoder: Decoder[UpdateRestaurant] = deriveDecoder[UpdateRestaurant]

  implicit lazy val updateRestaurantNameDecoder: Decoder[UpdateRestaurantName] = deriveDecoder[UpdateRestaurantName]
  implicit lazy val updateRestaurantNameEncoder: Encoder[UpdateRestaurantName] = deriveEncoder[UpdateRestaurantName]

  implicit lazy val updateRestaurantPhoneNumberDecoder: Decoder[UpdateRestaurantPhoneNumber] = deriveDecoder[UpdateRestaurantPhoneNumber]
  implicit lazy val updateRestaurantPhoneNumberEncoder: Encoder[UpdateRestaurantPhoneNumber] = deriveEncoder[UpdateRestaurantPhoneNumber]

  implicit lazy val updateRestaurantCuisinesDecoder: Decoder[UpdateRestaurantCuisines] = deriveDecoder[UpdateRestaurantCuisines]
  implicit lazy val updateRestaurantCuisinesEncoder: Encoder[UpdateRestaurantCuisines] = deriveEncoder[UpdateRestaurantCuisines]

  implicit lazy val updateRestaurantAddressDecoder: Decoder[UpdateRestaurantAddress] = deriveDecoder[UpdateRestaurantAddress]
  implicit lazy val updateRestaurantAddressEncoder: Encoder[UpdateRestaurantAddress] = deriveEncoder[UpdateRestaurantAddress]

  implicit lazy val updateRestaurantDescriptionDecoder: Decoder[UpdateRestaurantDescription] = deriveDecoder[UpdateRestaurantDescription]
  implicit lazy val updateRestaurantDescriptionEncoder: Encoder[UpdateRestaurantDescription] = deriveEncoder[UpdateRestaurantDescription]

  implicit lazy val allRestaurantsEncoder: Encoder[AllRestaurants.type] = deriveEncoder[AllRestaurants.type]
  implicit lazy val allRestaurantsDecoder: Decoder[AllRestaurants.type] = deriveDecoder[AllRestaurants.type]

  implicit lazy val restaurantsEncoder: Encoder[Restaurants] = deriveEncoder[Restaurants]
  implicit lazy val restaurantsDecoder: Decoder[Restaurants] = deriveDecoder[Restaurants]

  implicit def httpSuccessEncoder[A: Encoder]: Encoder[HttpSuccess[A]] = deriveEncoder[HttpSuccess[A]]
  implicit def httpSuccessDecoder[A: Decoder]: Decoder[HttpSuccess[A]] = deriveDecoder[HttpSuccess[A]]

  implicit lazy val httpFailureEncoder: Encoder[HttpFailure] = deriveEncoder[HttpFailure]
  implicit lazy val httpFailureDecoder: Decoder[HttpFailure] = deriveDecoder[HttpFailure]

  implicit lazy val updateActionDecoder: Decoder[UpdateAction] =
    List[Decoder[UpdateAction]](
      Decoder[UpdateRestaurant].widen,
      Decoder[UpdateRestaurantName].widen,
      Decoder[UpdateRestaurantPhoneNumber].widen,
      Decoder[UpdateRestaurantCuisines].widen,
      Decoder[UpdateRestaurantAddress].widen,
      Decoder[UpdateRestaurantDescription].widen
    ).reduceLeft(_ or _)

  implicit lazy val updateActionEncoder: Encoder[UpdateAction] = Encoder.instance {
    case a: UpdateRestaurant => a.asJson
    case a: UpdateRestaurantName => a.asJson
    case a: UpdateRestaurantPhoneNumber => a.asJson
    case a: UpdateRestaurantCuisines => a.asJson
    case a: UpdateRestaurantAddress => a.asJson
    case a: UpdateRestaurantDescription => a.asJson
  }
}
