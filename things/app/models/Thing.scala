package models

import java.util.UUID
import play.api.libs.json.Json
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError
import play.api.libs.json.JsResult
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import play.api.libs.json.Format
import java.util.Date

case class Thing(
  id: Option[UUID] = None,
  name: String,
  description: Option[String] = None,
  location: Option[String] = None,
  quantity: Int = 1,
  createdOn: Date = new Date(),
  lastUpdatedOn: Date = new Date())

object Thing {

  implicit object UUIDFormat extends Format[UUID] {
    def writes(uuid: UUID): JsValue = JsString(uuid.toString())
    def reads(json: JsValue): JsResult[UUID] = json match {
      case JsString(x) => JsSuccess(UUID.fromString(x))
      case _ => JsError("Expected UUID as JsString")
    }
  }

  implicit val thingWrite = Json.writes[Thing]

  implicit val thingRead = Json.reads[Thing]

  var things = Set(
    Thing(Some(UUID.randomUUID()), "Thing1", Option[String]("A thing of great import"), Option[String]("Kitchen")),
    Thing(Some(UUID.randomUUID()), "Thing2", Option[String]("Another thing"), Option[String]("Kitchen"), 2),
    Thing(Some(UUID.randomUUID()), "Thing3", Option[String]("Junk, just pure junk"), Option[String]("Basement")),
    Thing(Some(UUID.randomUUID()), "Thing4", Option[String]("Junk, just pure junk"), Option[String]("Basement"), 3))

  def findAll = this.things.toList.sortBy(_.name)

  def findById(id: UUID): Option[Thing] = this.things.find(_.id == Option[UUID](id))

  def findByName(name: String): Option[Thing] = this.things.find(_.name == name)

  def save(thing: Thing): Thing = {
    this.things.find(_.id == thing.id).map { oldThing =>
      this.things = this.things - oldThing + thing
      return thing
    }.getOrElse {
      val newThing = Thing(Some(UUID.randomUUID()), thing.name)
      this.things = this.things + newThing
      return newThing
    }
  }

}
