package com.augdim.console.backend.api.v1.auth

import com.augdim.backend.BadRequest
import com.augdim.backend.Unauthorized
import com.augdim.console.backend.datastore.loginDataStore
import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.routing.*
import com.augdim.backend.JWTAuthentication
import io.ktor.request.receive

// https://akveo.github.io/nebular/docs/auth/backend-api-endpoints#backend-auth-endpoints
class NebularUserParam(val email: String, val password: String)

fun Route.apiLogin() {
    route("login") {
        post {
            val userParam = call.receive<NebularUserParam>()
            val user = loginDataStore().get(userParam.email)
            val id =  if (user?.password == userParam.password) user.id else throw Unauthorized()
            val token = JWTAuthentication.createToken(id)
            call.response.header("Authorization", "Bearer $token")
            call.respond("{ \"token\": \"$token\" }")
        }
    }
}