package com.augdim.console.backend.api.v1

import com.augdim.backend.authenticateGae
import com.augdim.console.backend.api.v1.auth.apiAuth
import io.ktor.routing.Route
import io.ktor.routing.route

internal fun Route.apiV1() {
    route("v1") {
        apiAuth()
        authenticateGae {
//            intercept(ApplicationCallPipeline.Call) {
//                val userId = call.authentication.principal<JWTAuthentication.UserId>()!!.id
//                NamespaceManager.set(userId.toString())
//            }
            apiTemplate()
            apiView()
            apiCode()
        }
    }
}

