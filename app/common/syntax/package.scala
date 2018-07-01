package common

import cats.data.Validated.{Invalid, Valid}
import cats.data.ValidatedNel
import common.result.{HttpFailure, HttpSuccess}
import exceptions.ValidationException
import monix.eval.Task
import play.api.mvc.Result
import play.api.mvc.Results._
import io.circe.syntax._
import codecs._
import io.circe.Encoder
import play.api.libs.circe.CirceJsonWritableImplicits

package object syntax {

  implicit class CompleteResultOps[A: Encoder](val result: Task[ValidatedNel[ValidationException, A]])
    extends CirceJsonWritableImplicits {

    def playResult: Task[Result] = result.map {
      case Valid(a) => Ok(HttpSuccess(a).asJson)
      case Invalid(e) => BadRequest(HttpFailure(e.toList.map(_.getMessage)).asJson)
    }.onErrorHandle(ex => InternalServerError(HttpFailure(ex.getMessage :: Nil).asJson))
  }

  implicit class ResultOps[A: Encoder](val result: Task[A]) extends CirceJsonWritableImplicits {
    def playResult: Task[Result] =
      result.map(payload => Ok(HttpSuccess(payload).asJson))
        .onErrorHandle(ex => InternalServerError(HttpFailure(ex.getMessage :: Nil).asJson))
  }

}
