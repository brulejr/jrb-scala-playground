package models

import java.util.Date
import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json._

class Thing {
  var id: Long = _
  var name: String = _
  var quantity: Int = _
}

object Thing {

  def apply(id: Option[Long], name: String, quantity: Int) = {
    val t = new Thing
    t.id = id.getOrElse(-1)
    t.name = name
    t.quantity = quantity
    t
  }

  def unapply(t: Thing) = Some((Option(t.id), t.name, t.quantity))

  trait JSON {

    implicit val thingReads: Reads[Thing] = (
      (__ \ "id").readNullable[Long] and
      (__ \ "name").read[String] and
      (__ \ "quantity").read[Int])(Thing.apply _)

    implicit val thingWrites: Writes[Thing] = (
      (__ \ "id").writeNullable[Long] and
      (__ \ "name").write[String] and
      (__ \ "quantity").write[Int])(unlift(Thing.unapply))

    def parse(json: String) = Json.parse(json)

  }

}
