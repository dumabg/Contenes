package com.augdim.appconfig.backend.api.v1

import com.augdim.appconfig.backend.model.LoginModel
import com.augdim.backend.BadRequest
import com.augdim.backend.Unauthorized
import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.routing.*
import com.augdim.backend.JWTAuthentication

fun Route.apiLogin() {
    route("login") {
        post {
            val params = call.receiveParameters()
            val userParam =  params["u"] ?: throw BadRequest()
            val passwordParam = params["p"] ?: throw BadRequest()
            val user = LoginModel().get(userParam)
            val id =  if (user?.password == passwordParam) user.id else throw Unauthorized()
            val token = JWTAuthentication.createToken(id)
            call.response.header("Authorization", "Bearer $token")
            call.respond(id)
        }
    }
}