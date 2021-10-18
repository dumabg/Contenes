package com.augdim.console.backend.api.v1.auth

import com.augdim.backend.BadRequest
import com.augdim.console.backend.datastore.UserWithoutId
import com.augdim.console.backend.datastore.registerDataStore
import com.augdim.console.backend.model.UserAlreadyExistsException
import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.apiRegister() {
    route("register") {
        post {
            val params = call.receiveParameters()
            val userParam = params["u"] ?: throw BadRequest()
            val passwordParam = params["p"] ?: throw BadRequest()
            val user = UserWithoutId(userParam, passwordParam)
            try {
                registerDataStore().add(user)
                call.respond(true)
            }
            catch(_ : UserAlreadyExistsException) {
                call.respond(false)
            }
        }
    }
}