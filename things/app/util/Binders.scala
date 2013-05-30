package util

import java.util.UUID
import play.api.mvc.PathBindable

object Binders {
  
  implicit def uuidPathBinder = new PathBindable[UUID] {
    override def bind(key: String, value: String): Either[String, UUID] = {
      Right(UUID.fromString(value))
    }
    override def unbind(key: String, id: UUID): String = {
      id.toString
    }
  }
  
}