package com.augdim.appconfig.backend.api.v1

import com.augdim.appconfig.backend.model.ViewModel
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

fun Route.apiView() {
    route("views") {
        get {
            val views = ViewModel().getViews()
            call.respond(views)
        }
    }
}

