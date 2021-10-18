package com.augdim.appconfig.backend.api.v1

import com.augdim.appconfig.backend.model.AccessPoint
import com.augdim.appconfig.backend.model.AccessPointModel
import com.augdim.backend.BadRequest
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.accessPoint() {
    route("ap") {
        post {
            val params = call.receiveParameters()
            val id =  params["i"]?.toLong() ?: throw BadRequest()
            val idView = params["v"]?.toLong() ?: throw BadRequest()
            val apParams = params["p"]
//            val jsonView = call.receiveText()
//            val accessPoint = Gson().fromJson<AccessPoint>(jsonView, object : TypeToken<AccessPoint>() {}.type)
            AccessPointModel().add(AccessPoint(id, idView, apParams))
            call.response.status(HttpStatusCode.OK)
            call.respond("")
        }
        route("{idView}") {
            delete {
                val idView = call.parameters["idView"]?.toLong() ?: throw BadRequest()
                AccessPointModel().delete(idView)
                call.response.status(HttpStatusCode.OK)
            }
        }
    }
}