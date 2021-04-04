package service

import org.json.JSONObject
import persistence.GeoPointRepository
import persistence.client.PostgresClient

class GeoPointService {

    private val geoPointRepository = GeoPointRepository()

    fun getPoint(pointId: Int): JSONObject {
        PostgresClient()
        return geoPointRepository.getPoint(pointId)
    }

    fun updatePoint(pointId: Int, x: Float, y: Float) {
        PostgresClient()
        geoPointRepository.updatePoint(pointId, x, y)
    }

    fun insertPoint(pointId: Int, x: Float, y: Float) {
        PostgresClient()
        geoPointRepository.createPoint(pointId, x, y)
    }

    fun deletePoint(pointId: Int) {
        PostgresClient()
        geoPointRepository.deletePoint(pointId)
    }

    fun shapeIncludePoint(x: Float, y: Float): Boolean {
        PostgresClient()
        val point = "($x $y)"
        return geoPointRepository.shapeIncludePoint(point)
    }

}