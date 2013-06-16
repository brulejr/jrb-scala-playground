package models

import java.util.Date
import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json._

class Tag {
  var id: Long = _
  var name: String = _
  var description: String = _
  var createdOn: Date = _
  var lastUpdatedOn: Date = _
}

object Tag {

  def apply(
    id: Option[Long],
    name: String,
    description: Option[String],
    createdOn: Option[Date],
    lastUpdatedOn: Option[Date]) = {
    
    val t = new Tag
    t.id = id.getOrElse(-1)
    t.name = name
    t.description = description.getOrElse(null)
    t.createdOn = createdOn.getOrElse(null)
    t.lastUpdatedOn = lastUpdatedOn.getOrElse(null)
    t
  }

  def unapply(t: Tag) = Some(
      Option(t.id), 
      t.name, 
      Option(t.description),
      Option(t.createdOn), 
      Option(t.lastUpdatedOn))

  trait JSON {

    val timestampWrites: Writes[Date] = Writes.dateWrites("yyyy-MM-dd'T'HH:mm:ss.SSS")
    
    implicit val thingReads: Reads[Tag] = (
      (__ \ "id").readNullable[Long] and
      (__ \ "name").read[String] and
      (__ \ "description").readNullable[String] and
      (__ \ "createdOn").readNullable[Date] and
      (__ \ "lastUpdatedOn").readNullable[Date])(Tag.apply _)

    implicit val thingWrites: Writes[Tag] = (
      (__ \ "id").writeNullable[Long] and
      (__ \ "name").write[String] and
      (__ \ "description").writeNullable[String] and
      (__ \ "createdOn").writeNullable[Date](timestampWrites) and
      (__ \ "lastUpdatedOn").writeNullable[Date](timestampWrites))(unlift(Tag.unapply))

    def parse(json: String) = Json.parse(json)

  }

}
