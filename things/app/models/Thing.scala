package models

import java.util.Date
import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json._

class Thing {
  var id: Long = _
  var name: String = _
  var quantity: Int = _
  var description: String = _
  var location: String = _
  var lastSeenOn: Date = _
  var createdOn: Date = _
  var lastUpdatedOn: Date = _
}

object Thing {

  def apply(
    id: Option[Long],
    name: String,
    quantity: Int,
    description: Option[String],
    location: Option[String],
    lastSeenOn: Option[Date],
    createdOn: Option[Date],
    lastUpdatedOn: Option[Date]) = {
    
    val t = new Thing
    t.id = id.getOrElse(-1)
    t.name = name
    t.quantity = quantity
    t.description = description.getOrElse(null)
    t.location = location.getOrElse(null)
    t.lastSeenOn = lastSeenOn.getOrElse(null)
    t.createdOn = createdOn.getOrElse(null)
    t.lastUpdatedOn = lastUpdatedOn.getOrElse(null)
    t
  }

  def unapply(t: Thing) = Some(
      Option(t.id), 
      t.name, 
      t.quantity,
      Option(t.description),
      Option(t.location),
      Option(t.lastSeenOn), 
      Option(t.createdOn), 
      Option(t.lastUpdatedOn))

  trait JSON {

    val dateWrites: Writes[Date] = Writes.dateWrites("yyyy-MM-dd")
    val timestampWrites: Writes[Date] = Writes.dateWrites("yyyy-MM-dd'T'HH:mm:ss.SSS")
    
    implicit val thingReads: Reads[Thing] = (
      (__ \ "id").readNullable[Long] and
      (__ \ "name").read[String] and
      (__ \ "quantity").read[Int] and
      (__ \ "description").readNullable[String] and
      (__ \ "location").readNullable[String] and
      (__ \ "lastSeenOn").readNullable[Date] and
      (__ \ "createdOn").readNullable[Date] and
      (__ \ "lastUpdatedOn").readNullable[Date])(Thing.apply _)

    implicit val thingWrites: Writes[Thing] = (
      (__ \ "id").writeNullable[Long] and
      (__ \ "name").write[String] and
      (__ \ "quantity").write[Int] and
      (__ \ "description").writeNullable[String] and
      (__ \ "location").writeNullable[String] and
      (__ \ "lastSeenOn").writeNullable[Date](dateWrites) and
      (__ \ "createdOn").writeNullable[Date](timestampWrites) and
      (__ \ "lastUpdatedOn").writeNullable[Date](timestampWrites))(unlift(Thing.unapply))

    def parse(json: String) = Json.parse(json)

  }

}
