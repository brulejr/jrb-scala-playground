package persistence

import org.mybatis.scala.mapping._
import org.mybatis.scala.mapping.Binding._
import org.mybatis.scala.mapping.TypeHandlers._
import models.Thing

object ThingDAO {

  val ThingResultMap = new ResultMap[Thing] {
    id(column = "id", property = "id")
    result(column = "name", property = "name")
    result(column = "quantity", property = "quantity", jdbcType = JdbcType.INTEGER)
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
        INSERT INTO thing(name, quantity)
        VALUES (
        	{ "name"? },
        	{ ?("quantity", jdbcType = JdbcType.INTEGER) }
        )
      </xsql>
  }

  val update = new Update[Thing] {
    def xsql =
      <xsql>
        UPDATE thing
        SET 
          name = { "name"? },
          quantity = { ?("quantity", jdbcType = JdbcType.INTEGER) }
        WHERE id ={ "id"? }
      </xsql>
  }

  def bind = Seq(delete, deleteById, findById, findByName, findAll, insert, update)

}