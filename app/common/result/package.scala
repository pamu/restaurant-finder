package common

package object result {
  sealed trait HttpResult
  case class HttpSuccess[A](result: A) extends HttpResult
  case class HttpFailure(msgs: List[String]) extends HttpResult
}
