package com.augdim.app.backend.api.v1

import com.augdim.backend.BadRequest
import com.augdim.backend.NotFound
import com.augdim.app.backend.model.ViewModel
import com.google.appengine.api.NamespaceManager
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.apiView() {
    route("view/{userId}/{ap}") {
        get {
            val userId = call.parameters["userId"]?.toLongOrNull() ?: throw BadRequest()
            NamespaceManager.set(userId.toString())
            val accessPoint = call.parameters["ap"]?.toLongOrNull() ?: throw BadRequest()
            val view = ViewModel().getView(accessPoint) ?: throw NotFound("AccessPoint " + accessPoint + "not found")
            call.respond(view)
        }
    }
}