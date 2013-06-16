package persistence

import org.mybatis.scala.mapping._
import org.mybatis.scala.mapping.Binding._
import org.mybatis.scala.mapping.TypeHandlers._
import models.Thing
import java.util.Date
import org.mybatis.scala.session.Session

object ThingDAO {

  val ThingResultMap = new ResultMap[Thing] {
    id(column = "id", property = "id")
    result(column = "name", property = "name")
    result(column = "quantity", property = "quantity", jdbcType = JdbcType.INTEGER)
    result(column = "description", property = "description")
    result(column = "location", property = "location")
    result(column = "last_seen_on", property = "lastSeenOn", jdbcType = JdbcType.TIMESTAMP)
    result(column = "created_on", property = "createdOn", jdbcType = JdbcType.TIMESTAMP)
    result(column = "last_updated_on", property = "lastUpdatedOn", jdbcType = JdbcType.TIMESTAMP)
  }

  val SELECT_SQL =
    <xsql>
      SELECT *
      FROM thing
    </xsql>

  val DELETE_SQL =
    <xsql>
      DELETE FROM thing
      WHERE id = { "id"? }
    </xsql>

  val delete = new Delete[Thing] {
    def xsql = DELETE_SQL
  }

  val deleteById = new Delete[Long] {
    def xsql = DELETE_SQL
  }

  val findAll = new SelectList[Thing] {
    resultMap = ThingResultMap
    def xsql = SELECT_SQL
  }

  val findById = new SelectOneBy[Long, Thing] {
    resultMap = ThingResultMap
    def xsql =
      <xsql>
        { SELECT_SQL }
        WHERE id ={ "id"? }
      </xsql>
  }

  val findByName = new SelectListBy[String, Thing] {
    resultMap = ThingResultMap
    def xsql =
      <xsql>
        { SELECT_SQL }
        WHERE name_ LIKE{ "name"? }
        ORDER BY name_
      </xsql>
  }

  val insert = new Insert[Thing] {
    keyGenerator = JdbcGeneratedKey(null, "id")
    def xsql =
      <xsql>
        INSERT INTO thing(name, quantity, description, location, last_seen_on, created_on, last_updated_on)
        VALUES (
        	{ "name"? },
        	{ ?("quantity", jdbcType = JdbcType.INTEGER) },
        	{ ?("description", jdbcType = JdbcType.VARCHAR) },
        	{ ?("location", jdbcType = JdbcType.VARCHAR) },
        	{ ?("lastSeenOn", jdbcType = JdbcType.TIMESTAMP) },
        	{ ?("createdOn", jdbcType = JdbcType.TIMESTAMP) },
        	{ ?("lastUpdatedOn", jdbcType = JdbcType.TIMESTAMP) }
        )
      </xsql>

    override def apply(thing: Thing)(implicit s: Session): Int = {
      thing.createdOn = new Date()
      thing.lastUpdatedOn = new Date()
      super.apply(thing)
    }
  }

  val update = new Update[Thing] {
    def xsql =
      <xsql>
        UPDATE thing
        SET 
          name = { "name"? },
          quantity = { ?("quantity", jdbcType = JdbcType.INTEGER) },
          last_seen_on = { ?("lastSeenOn", jdbcType = JdbcType.TIMESTAMP) }
          last_updated_on = { ?("lastUpdatedOn", jdbcType = JdbcType.TIMESTAMP) }
        WHERE id = { "id"? }
      </xsql>

    override def apply(thing: Thing)(implicit s: Session): Int = {
      thing.lastUpdatedOn = new Date()
      super.apply(thing)
    }
  }

  def bind = Seq(delete, deleteById, findById, findByName, findAll, insert, update)

}