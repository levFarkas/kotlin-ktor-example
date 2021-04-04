package persistence

import model.Point
import model.Status
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.json.JSONObject

class GeoPointRepository{
    fun getPoint(pointId: Int) : JSONObject {
        val result = JSONObject()
        transaction {
            val query = Point.select { Point.pointId eq pointId }
            query.forEach {
                if (it.hasValue(Point.x) && it.hasValue(Point.y)){
                    result.put("x", it[Point.x])
                    result.put("y", it[Point.y])
                    result.put("status", Status.SUCCESS)
                }
            }
        }
        if (result.length() != 0) {
            return result
        }
        val json = JSONObject()
        json.append("status", Status.FAILED)
        return json
    }

    fun createPoint(pointId: Int, x: Float, y: Float) {
        transaction {
            Point.insert {
                it[Point.pointId] = pointId
                it[Point.x] = x
                it[Point.y] = y
            }
        }
    }

    fun updatePoint(pointId: Int, x: Float, y: Float) {
        transaction {
            Point.update({ Point.pointId eq pointId }) {
                it[Point.x] = x
                it[Point.y] = y
            }
        }
    }

    fun deletePoint(pointId: Int) {
        transaction {
            Point.deleteWhere { Point.pointId eq pointId }
        }
    }

    fun shapeIncludePoint(point: String): Boolean {
        val query = "SELECT gid, st_contains(geom, ST_GeomFromText('POINT$point', 4326)) as result FROM shape"
        var found = false
        transaction {
            TransactionManager.current().exec(query) { rs ->
                while (rs.next()) {
                    if (!found) {
                        found = rs.getBoolean("result")
                    }
                }
            }
        }
        return found
    }

}