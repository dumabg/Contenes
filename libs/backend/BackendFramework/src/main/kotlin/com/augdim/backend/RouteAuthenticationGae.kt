package com.augdim.backend

import com.google.appengine.api.NamespaceManager
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.response.header
import io.ktor.routing.Route

fun Route.authenticateGae(
    build: Route.() -> Unit
    ): Route {
    return authenticate {
        intercept(ApplicationCallPipeline.Call) {
            val userId = call.authentication.principal<JWTAuthentication.UserId>()!!.id
            NamespaceManager.set(userId.toString())
            val token = JWTAuthentication.createToken(userId)
            call.response.header("Authorization", "Bearer $token")
        }
        build.invoke(this)
    }
}