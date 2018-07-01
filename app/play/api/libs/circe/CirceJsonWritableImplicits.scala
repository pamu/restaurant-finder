package play.api.libs.circe

import io.circe._
import play.api.http._
import play.api.libs.ws.{BodyWritable, InMemoryBody}
import play.api.mvc._
import io.circe.syntax._

trait CirceJsonWritableImplicits {

  val defaultPrinter: Printer = Printer.noSpaces

  implicit val contentTypeOf_Json: ContentTypeOf[Json] = {
    ContentTypeOf(Some(ContentTypes.JSON))
  }

  implicit def writableOf_Json(implicit codec: Codec, printer: Printer = defaultPrinter): Writeable[Json] = {
    Writeable(a => codec.encode(a.pretty(printer)))
  }

  implicit def bodyWritableOf_Json(implicit codec: Codec, printer: Printer = defaultPrinter): BodyWritable[Json] = {
    BodyWritable(a => InMemoryBody(codec.encode(a.pretty(printer))), ContentTypes.JSON)
  }

  implicit def bodyWritableOf[A: Encoder](implicit codec: Codec, printer: Printer = defaultPrinter): BodyWritable[A] = {
    BodyWritable(a => InMemoryBody(codec.encode(a.asJson.pretty(printer))), ContentTypes.JSON)
  }
}

object CirceJsonWritable extends CirceJsonWritableImplicits
