import com.papsign.ktor.openapigen.OpenAPIGen
import com.papsign.ktor.openapigen.openAPIGen
import com.papsign.ktor.openapigen.schema.namer.DefaultSchemaNamer
import com.papsign.ktor.openapigen.schema.namer.SchemaNamer
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.netty.EngineMain
import model.Status
import org.json.JSONObject
import service.GeoPointService
import kotlin.reflect.KType

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused", "CAST_NEVER_SUCCEEDS") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val geoPointService = GeoPointService()

    install(OpenAPIGen) {
        info {
            version = "0.0.1"
            title = "Test API"
            description = "The Test API"
            contact {
                name = "Support"
                email = "support@test.com"
            }
        }
        // describe the server, add as many as you want
        server("http://localhost:8080/") {
            description = "Test server"
        }
        //optional custom schema object namer
        replaceModule(DefaultSchemaNamer, object: SchemaNamer {
            val regex = Regex("[A-Za-z0-9_.]+")
            override fun get(type: KType): String {
                return type.toString().replace(regex) { it.value.split(".").last() }.replace(Regex(">|<|, "), "_")
            }
        })
    }

    routing {

        get("/") {
            call.respondRedirect("/swagger-ui/index.html?url=/openapi.json", true)
        }

        /*
        TODO openapi json generation is not ready for now.
         */
        get("/openapi.json") {
            call.respond(openAPIGen.api.serialize())
        }

        get("/get/{pointId}") {
            val result = geoPointService.getPoint(call.parameters["pointId"]!!.toInt())
            if (result["status"] == Status.SUCCESS) {
                call.respondText { "(${result["x"]},${result["y"]})" }
            }
            call.respondText { "Not found point" }
        }
    }
    routing {
        post("/update") {
            val requestBody = call.receiveText()
            val json = JSONObject(requestBody)
            if (json.has("pointId") && json.has("x") && json.has("y")) {
                geoPointService.updatePoint(
                    pointId = json.getInt("pointId"),
                    x = json.getFloat("x"),
                    y = json.getFloat("y")
                )
                call.respondText { "Successful update" }
            }
            call.respondText { "Not valid post body! Need pointId: Int, x: Int, y: Int" }
        }
    }
    routing {
        get("/delete/{pointId}") {
            val pointId = call.parameters["pointId"]!!.toInt()
            val result = geoPointService.getPoint(pointId)
            if (result["status"] != Status.FAILED) {
                geoPointService.deletePoint(pointId)
                call.respondText { "Successful delete" }
            } else {
                call.respondText { "$pointId does not found in the database" }
            }
        }
    }
    routing {
        post("/create") {
            val requestBody = call.receiveText()
            val json = JSONObject(requestBody)
            if (json.has("pointId") && json.has("x") && json.has("y")) {
                geoPointService.insertPoint(
                    pointId = json.getInt("pointId"),
                    x = json.getFloat("x"),
                    y = json.getFloat("y")
                )
                call.respondText { "Successful insert" }
            }
            call.respondText { "Not valid post body! Need pointId: Int, x: Int, y: Int" }
        }
    }
    routing {
        get("/contains/{pointId}") {
            val pointId = call.parameters["pointId"]!!.toInt()
            val result = geoPointService.getPoint(pointId)
            if (result["status"] == Status.SUCCESS) {
                val found = geoPointService.shapeIncludePoint(result.getFloat("x"), result.getFloat("y"))
                call.respondText { "Found: $found" }
            } else {
                call.respondText { "$pointId does not found in the database" }
            }
        }

    }
}