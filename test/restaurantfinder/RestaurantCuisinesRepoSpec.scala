package restaurantfinder

import org.scalatest.GivenWhenThen
import org.scalatestplus.play.PlaySpec
import providers.RestaurantFinderPerSuiteProvider

import scala.concurrent.duration._
import akka.util.Timeout
import domain.{Restaurant, RestaurantData}
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Await

class RestaurantCuisinesRepoSpec extends PlaySpec
  with RestaurantFinderPerSuiteProvider
  with GivenWhenThen {

  "RestaurantCuisinesRepoSpec" must {

    "save restaurant data" in {
      Given("restaurant data")
      val restaurantData = RestaurantData(
        "RigaRest",
        "948438484",
        List(
          "Cuisine1",
          "Cuisine2",
          "Cuisine3"
        ),
        "Riga, Latvia",
        "Foobar is best in town with lot of cuisines"
      )

      When("data is saved in database")
      runTask(repo.saveRestaurantData(restaurantData))

      Then("data can be retrieved")
      val restaurant = restaurantBy(_.name === "RigaRest").head

      restaurant.name mustBe "RigaRest"
      restaurant.phoneNumber mustBe "948438484"
      restaurant.address mustBe  "Riga, Latvia"
      restaurant.description mustBe  "Foobar is best in town with lot of cuisines"
      restaurant.cuisines.map(_.name) must contain theSameElementsAs List("Cuisine1", "Cuisine2", "Cuisine3")
    }

    "add any number of restaurants" in {
      Given("restaurant data")
      val restaurantData = RestaurantData(
        "BerlinRest",
        "239394297454",
        List(
          "Cuisine4",
          "Cuisine5",
          "Cuisine6"
        ),
        "Berlin, Germany",
        "BerlinRest is best in town with lot of cuisines"
      )

      When("data is saved in database")
      runTask(repo.saveRestaurantData(restaurantData))

      Then("data can be retrieved")
      val restaurant = restaurantBy(_.name === "BerlinRest").head

      restaurant.name mustBe "BerlinRest"
      restaurant.phoneNumber mustBe "239394297454"
      restaurant.address mustBe  "Berlin, Germany"
      restaurant.description mustBe  "BerlinRest is best in town with lot of cuisines"
      restaurant.cuisines.map(_.name) must contain theSameElementsAs List("Cuisine4", "Cuisine5", "Cuisine6")
    }

    "fetch restaurant data by id" in {
      Given("restaurant already in database with id")
      val restaurant = restaurantBy(_ => true).head
      When("restaurant is retrieved with same id")
      val theSameRestaurant = restaurantBy(_.id === restaurant.id)
      Then("is it found")
      restaurant.id mustBe theSameRestaurant.head.id
    }

    "fetch all restaurant data" in {
      restaurantBy(_ => true).map(_.name) must contain allElementsOf List("RigaRest", "BerlinRest")
    }

    "update restaurant data" in {
      Given("id of a restaurant")
      val id = restaurantBy(_.name === "RigaRest").head.id
      When("its data is updated")
      runTask(repo.updateRestaurant(id, { data =>
        data.copy(
          name = "RigaRest1",
          phoneNumber = "1",
          cuisines = List("foo", "bar", "baz"),
          address = "Riga street1",
          description = "Bring awesomeness to your life in Riga"
        )
      }))

      Then("restaurant can be found with new name")
      val restaurant = restaurantBy(_.name === "RigaRest1").head
      And("id is not changed")
      restaurant.id mustBe id
      And("all changes are preserved")
      restaurant.name mustBe "RigaRest1"
      restaurant.phoneNumber mustBe "1"
      restaurant.address mustBe  "Riga street1"
      restaurant.description mustBe  "Bring awesomeness to your life in Riga"
      restaurant.cuisines.map(_.name) must contain theSameElementsAs
        List("foo", "bar", "baz")
    }

    "delete restaurant data" in {
      Given("restaurant id")
      val id = restaurantBy(_.name === "BerlinRest").head.id
      When("repo delete is called")
      runTask(repo.deleteRestaurants(id))
      Then("restaurant with the id is not available")
      restaurantBy(_.id === id).isEmpty mustBe true
    }

  }

  // assuming allRestaurants call is not buggy :)
  def restaurantBy(f: Restaurant => Boolean): Seq[Restaurant] =
    runTask(repo.allRestaurants).filter(f)

  def runTask[A](task: Task[A]): A = Await.result(task.runAsync, 5.seconds)

  // private

  private val repo = components.repo
}
