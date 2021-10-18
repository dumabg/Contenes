package com.augdim.console.backend.api.v1.auth

import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.apiAuth() {
    route("auth") {
        apiRegister()
        apiLogin()
    }
}