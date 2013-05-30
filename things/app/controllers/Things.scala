package controllers

import play.api._
import play.api.mvc._
import models.Thing
import play.api.libs.json.Json
import java.util.UUID
import play.api.libs.json.JsError

object Things extends Controller {
  
  def create = Action(parse.json) { implicit request =>
    request.body.validate[Thing].map{ 
      case thing => {
        Ok(Json.toJson(Thing.save(Thing(None, thing.name))))
      }
    }.recoverTotal{
      e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
    }
  }
  
  def list = Action { implicit request =>
    val things = Thing.findAll
    Ok(Json.toJson(things))
  }

  def showById(id: UUID) = Action { implicit request =>
    Thing.findById(id).map { thing =>
      Ok(Json.toJson(thing))
    }.getOrElse(NotFound)
  }

  def showByName(name: String) = Action { implicit request =>
    Thing.findByName(name).map { thing =>
      Ok(Json.toJson(thing))
    }.getOrElse(NotFound)
  }
  
  def update(id: UUID) = Action(parse.json) { implicit request =>
    request.body.validate[Thing].map{ 
      case thing => {
    	Thing.findById(id).map { existingThing =>
    		Ok(Json.toJson(Thing.save(Thing(Option[UUID](id), thing.name))))
    	}.getOrElse(BadRequest("Unknown thing"))
      }
    }.recoverTotal{
      e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
    }
  }

}