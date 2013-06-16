package controllers

import models.Thing
import persistence.PersistenceContext._
import persistence.ThingDAO
import play.api._
import play.api.libs.json.JsError
import play.api.libs.json.Json
import play.api.mvc._

object Things extends Controller with Thing.JSON {

  def list = Action { implicit request =>
    withTransaction { implicit session =>
      Ok(Json.toJson(ThingDAO.findAll()))
    }
  }

  def create = Action(parse.json) { implicit request =>
    request.body.validate[Thing].map {
      case thing => {
        withTransaction { implicit session =>
          ThingDAO insert thing
          Ok(Json.toJson(thing)).withHeaders(
              LOCATION -> routes.Things.findById(thing.id).absoluteURL(false))
        }
      }
    }.recoverTotal {
      e => BadRequest("Detected error:" + JsError.toFlatJson(e))
    }
  }

  def deleteById(id: Long) = Action { implicit request =>
    withTransaction { implicit session =>
      ThingDAO.deleteById(id) match {
        case 0 => NotFound
        case _ => Ok(Json.toJson("SUCCESS"))
      }

    }
  }

  def findById(id: Long) = Action { implicit request =>
    withTransaction { implicit session =>
      ThingDAO.findById(id) match {
        case Some(thing: Thing) => Ok(Json.toJson(thing))
        case _ => NotFound
      }
    }
  }

  def update(id: Long) = Action(parse.json) { implicit request =>
    request.body.validate[Thing].map {
      case thing => {
        withTransaction { implicit session =>
          thing.id = id
          ThingDAO.update(thing) match {
            case 0 => BadRequest("Unable to update thing")
            case _ => Ok(Json.toJson(ThingDAO.findById(thing.id)))
          }
        }
      }
    }.recoverTotal {
      e => BadRequest("Detected error:" + JsError.toFlatJson(e))
    }
  }

}