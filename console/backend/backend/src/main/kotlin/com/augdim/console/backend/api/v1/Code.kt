package com.augdim.console.backend.api.v1

import com.augdim.backend.BadRequest
import com.augdim.backend.userId
import com.augdim.console.backend.datastore.codeDataStore
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.apiCode() {
    route("code") {
        route("{num}") {
            post {
                val num = call.parameters["num"]?.toIntOrNull() ?: throw BadRequest()
                val codes = codeDataStore().createCodes(call.userId, num)
                call.respond(codes)
            }
        }
    }
}