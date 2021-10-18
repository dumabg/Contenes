package com.augdim.backend

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.auth.Principal
import io.ktor.auth.authentication
import java.util.*

object JWTAuthentication {
    private lateinit var issuer: String
    private lateinit var algorithm: Algorithm
    private var validityInMs: Long = 0
    private lateinit var _verifier: JWTVerifier
    val verifier get() = _verifier


    @Suppress("EXPERIMENTAL_API_USAGE")
    fun init(application: Application) {
        with(application.environment.config) {
            issuer = property("jwt.issuer").getString()
            algorithm = Algorithm.HMAC512(property("jwt.secret").getString())
            validityInMs = property("jwt.validityInMs").getString().toLong()
            _verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
        }
    }

    fun createToken(id: Long): String = JWT.create()
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withClaim("id", id)
            .withExpiresAt(Date(System.currentTimeMillis() + validityInMs))
            .sign(algorithm)

    class UserId(val id: Long): Principal
}

val ApplicationCall.userId get() = authentication.principal<JWTAuthentication.UserId>()!!.id
