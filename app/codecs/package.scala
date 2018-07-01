import common.result.{HttpFailure, HttpSuccess}
import domain.actions.{CreateRestaurant, UpdateRestaurant}
import domain.{Cuisine, CuisineId, Restaurant, RestaurantId}
import io.circe._
import io.circe.generic.semiauto._
import domain.query._

package object codecs {
  implicit lazy val cuisineIdEncoder: Encoder[CuisineId] = deriveEncoder[CuisineId]
  implicit lazy val cuisineIdDecoder: Decoder[CuisineId] = deriveDecoder[CuisineId]

  implicit lazy val restaurantIdEncoder: Encoder[RestaurantId] = deriveEncoder[RestaurantId]
  implicit lazy val restaurantIdDecoder: Decoder[RestaurantId] = deriveDecoder[RestaurantId]

  implicit lazy val cuisineEncoder: Encoder[Cuisine] = deriveEncoder[Cuisine]
  implicit lazy val cuisineDecoder: Decoder[Cuisine] = deriveDecoder[Cuisine]

  implicit lazy val restaurantEncoder: Encoder[Restaurant] = deriveEncoder[Restaurant]
  implicit lazy val restaurantDecoder: Decoder[Restaurant] = deriveDecoder[Restaurant]

  implicit lazy val createRestaurantEncoder: Encoder[CreateRestaurant] = deriveEncoder[CreateRestaurant]
  implicit lazy val createRestaurantDecoder: Decoder[CreateRestaurant] = deriveDecoder[CreateRestaurant]

  implicit lazy val updateRestaurantEncoder: Encoder[UpdateRestaurant] = deriveEncoder[UpdateRestaurant]
  implicit lazy val updateRestaurantDecoder: Decoder[UpdateRestaurant] = deriveDecoder[UpdateRestaurant]

  implicit lazy val allRestaurantsEncoder: Encoder[AllRestaurants.type] = deriveEncoder[AllRestaurants.type]
  implicit lazy val allRestaurantsDecoder: Decoder[AllRestaurants.type] = deriveDecoder[AllRestaurants.type]

  implicit lazy val restaurantsEncoder: Encoder[Restaurants] = deriveEncoder[Restaurants]
  implicit lazy val restaurantsDecoder: Decoder[Restaurants] = deriveDecoder[Restaurants]

  implicit def httpSuccessEncoder[A: Encoder]: Encoder[HttpSuccess[A]] = deriveEncoder[HttpSuccess[A]]
  implicit def httpSuccessDecoder[A: Decoder]: Decoder[HttpSuccess[A]] = deriveDecoder[HttpSuccess[A]]

  implicit lazy val httpFailureEncoder: Encoder[HttpFailure] = deriveEncoder[HttpFailure]
  implicit lazy val httpFailureDecoder: Decoder[HttpFailure] = deriveDecoder[HttpFailure]
}
