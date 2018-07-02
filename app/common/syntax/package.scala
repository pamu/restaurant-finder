package common

import common.http.result.{HttpFailure, HttpSuccess}
import common.results.Result
import exceptions.{AppException, ResourceNotFoundException, RuntimeServerException, ValidationException}
import io.circe.Encoder
import io.circe.syntax._
import monix.eval.Task
import play.api.mvc.Results._
import play.api.mvc.{Result => PlayResult}
import codecs._
import play.api.Logger
import play.api.libs.circe.CirceJsonWritable._

package object syntax {

  implicit class ResultOps[A: Encoder](val result: Result[A]) {
    def playResult: Task[PlayResult] =
      result.map(payload => Ok(HttpSuccess(payload).asJson))
        .onErrorHandle {
          case aex: AppException => handleAppException(aex)
          case ex: Throwable => InternalServerError(HttpFailure(ex.getMessage).asJson)
        }
  }

  def handleAppException(ex: AppException): PlayResult = {
    Logger.error(ex.getMessage)
    ex match {
      case ex: ValidationException => BadRequest(HttpFailure(ex.getMessage).asJson)
      case ex: ResourceNotFoundException => NotFound(HttpFailure(ex.getMessage).asJson)
      case ex: RuntimeServerException => InternalServerError(HttpFailure(ex.getMessage).asJson)
    }
  }

}
