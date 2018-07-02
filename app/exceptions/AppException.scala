package exceptions

sealed trait AppException extends Throwable

abstract class ValidationException(msg: String) extends Exception(msg) with AppException
abstract class ResourceNotFoundException(msg: String) extends Exception(msg) with AppException
abstract class RuntimeServerException(msg: String) extends Exception(msg) with AppException
case class RestaurantIdNotFound(msg: String) extends ResourceNotFoundException(msg)
case class EmptyString(msg: String) extends ValidationException(msg)