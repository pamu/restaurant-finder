package common

import cats.data.ValidatedNel
import exceptions.ValidationException
import monix.eval.Task

package object results {
  type Validation[A] = ValidatedNel[ValidationException, A]
  type Result[A] = Task[A]
}
