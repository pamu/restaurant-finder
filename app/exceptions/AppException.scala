package exceptions

trait AppException extends Throwable

abstract class ValidationException(msg: String) extends Exception(msg) with AppException

abstract class UnExpectedException(msg: String) extends Exception(msg) with AppException

case class RestaurantIdNotFound(msg: String) extends ValidationException(msg)