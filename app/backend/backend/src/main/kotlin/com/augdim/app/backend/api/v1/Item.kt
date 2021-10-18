package com.augdim.app.backend.api.v1

import com.augdim.app.backend.model.ItemModel
import com.augdim.backend.BadRequest
import com.google.appengine.api.NamespaceManager
import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.apiItem() {
    route("item") {
        post {
            val params = call.receiveParameters()
            val userId = params["userId"]?.toLongOrNull() ?: throw BadRequest()
            NamespaceManager.set(userId.toString())
            val itemId = params["itemId"]?.toLong() ?: throw BadRequest()
            val templateId = params["templateId"]?.toLong() ?: throw BadRequest()
            val result = ItemModel().execute(templateId, itemId)
            call.respond(result)
        }
    }
}