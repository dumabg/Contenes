package com.augdim.backend

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.error

fun Application.configureKtor() {
    if (isDev) {
        log.info("Develepment mode")
        install(CallLogging)
    }
    install(ContentNegotiation) {
        gson {
            // Configure Gson here
        }
    }
    install(DefaultHeaders)
    install(ConditionalHeaders)
    install(Compression)
    install(PartialContent)
    install(AutoHeadResponse)
//    install(WebSockets)
    install(XForwardedHeaderSupport)
    install(StatusPages) {
        exception<ServiceUnavailable> {
            call.respond(HttpStatusCode.ServiceUnavailable)
        }
        exception<BadRequest> {
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<Unauthorized> {
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<NotFound> {
            call.respond(HttpStatusCode.NotFound)
        }
        exception<SecretInvalidError> {
            call.respond(HttpStatusCode.Forbidden)
        }
        exception<Throwable> { cause ->
            environment.log.error(cause)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

//    install(ContentNegotiation) {
//        register(ContentType.Application.Json, KotlinxConverter())
//    }

    install(Authentication) {
        jwt {
            JWTAuthentication.init(this@configureKtor)
            verifier(JWTAuthentication.verifier)
            validate {
                it.payload.getClaim("id").asLong()?.let { JWTAuthentication.UserId(it) }
            }
        }
    }
}