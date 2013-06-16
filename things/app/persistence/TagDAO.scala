package persistence

import org.mybatis.scala.mapping._
import org.mybatis.scala.mapping.Binding._
import org.mybatis.scala.mapping.TypeHandlers._
import models.Tag
import java.util.Date
import org.mybatis.scala.session.Session

object TagDAO {

  val TagResultMap = new ResultMap[Tag] {
    id(column = "id", property = "id")
    result(column = "name", property = "name")
    result(column = "description", property = "description")
    result(column = "created_on", property = "createdOn", jdbcType = JdbcType.TIMESTAMP)
    result(column = "last_updated_on", property = "lastUpdatedOn", jdbcType = JdbcType.TIMESTAMP)
  }

  val SELECT_SQL =
    <xsql>
      SELECT *
      FROM tag
    </xsql>

  val DELETE_SQL =
    <xsql>
      DELETE FROM tag
      WHERE id = { "id"? }
    </xsql>

  val delete = new Delete[Tag] {
    def xsql = DELETE_SQL
  }

  val deleteById = new Delete[Long] {
    def xsql = DELETE_SQL
  }

  val findAll = new SelectList[Tag] {
    resultMap = TagResultMap
    def xsql = SELECT_SQL
  }

  val findById = new SelectOneBy[Long, Tag] {
    resultMap = TagResultMap
    def xsql =
      <xsql>
        { SELECT_SQL }
        WHERE id ={ "id"? }
      </xsql>
  }

  val findByName = new SelectListBy[String, Tag] {
    resultMap = TagResultMap
    def xsql =
      <xsql>
        { SELECT_SQL }
        WHERE name_ LIKE{ "name"? }
        ORDER BY name_
      </xsql>
  }

  val insert = new Insert[Tag] {
    keyGenerator = JdbcGeneratedKey(null, "id")
    def xsql =
      <xsql>
        INSERT INTO tag(name, description, created_on, last_updated_on)
        VALUES (
        	{ "name"? },
        	{ ?("description", jdbcType = JdbcType.VARCHAR) },
        	{ ?("createdOn", jdbcType = JdbcType.TIMESTAMP) },
        	{ ?("lastUpdatedOn", jdbcType = JdbcType.TIMESTAMP) }
        )
      </xsql>

    override def apply(tag: Tag)(implicit s: Session): Int = {
      tag.createdOn = new Date()
      tag.lastUpdatedOn = new Date()
      super.apply(tag)
    }
  }

  val update = new Update[Tag] {
    def xsql =
      <xsql>
        UPDATE tag
        SET 
          name = { "name"? },
          description = { "description"? },
          last_updated_on = { ?("lastUpdatedOn", jdbcType = JdbcType.TIMESTAMP) }
        WHERE id = { "id"? }
      </xsql>

    override def apply(tag: Tag)(implicit s: Session): Int = {
      tag.lastUpdatedOn = new Date()
      super.apply(tag)
    }
  }

  def bind = Seq(delete, deleteById, findById, findByName, findAll, insert, update)

}