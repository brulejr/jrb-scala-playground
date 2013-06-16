package controllers

import models.Tag
import persistence.PersistenceContext._
import persistence.TagDAO
import play.api._
import play.api.libs.json.JsError
import play.api.libs.json.Json
import play.api.mvc._

object Tags extends Controller with Tag.JSON {

  def list = Action { implicit request =>
    withTransaction { implicit session =>
      Ok(Json.toJson(TagDAO.findAll()))
    }
  }

  def create = Action(parse.json) { implicit request =>
    request.body.validate[Tag].map {
      case tag => {
        withTransaction { implicit session =>
          TagDAO insert tag
          Ok(Json.toJson(tag)).withHeaders(
              LOCATION -> routes.Tags.findById(tag.id).absoluteURL(false))
        }
      }
    }.recoverTotal {
      e => BadRequest("Detected error:" + JsError.toFlatJson(e))
    }
  }

  def deleteById(id: Long) = Action { implicit request =>
    withTransaction { implicit session =>
      TagDAO.deleteById(id) match {
        case 0 => NotFound
        case _ => Ok(Json.toJson("SUCCESS"))
      }

    }
  }

  def findById(id: Long) = Action { implicit request =>
    withTransaction { implicit session =>
      TagDAO.findById(id) match {
        case Some(tag: Tag) => Ok(Json.toJson(tag))
        case _ => NotFound
      }
    }
  }

  def update(id: Long) = Action(parse.json) { implicit request =>
    request.body.validate[Tag].map {
      case tag => {
        withTransaction { implicit session =>
          tag.id = id
          TagDAO.update(tag) match {
            case 0 => BadRequest("Unable to update tag")
            case _ => Ok(Json.toJson(TagDAO.findById(tag.id)))
          }
        }
      }
    }.recoverTotal {
      e => BadRequest("Detected error:" + JsError.toFlatJson(e))
    }
  }

}