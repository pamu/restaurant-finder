package common.http

package object result {

  sealed trait HttpResult
  case class HttpSuccess[A](result: A) extends HttpResult
  case class HttpFailure(errors: List[String]) extends HttpResult
  object HttpFailure {
    def apply(msg: String): HttpFailure = HttpFailure(msg :: Nil)
  }

}
