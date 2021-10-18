package com.augdim.console.backend

import com.augdim.backend.configureKtor
import com.augdim.backend.isDev
import com.augdim.console.backend.api.v1.apiV1
import com.augdim.console.backend.datastore.dataStoreInit
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.routing.Routing
import io.ktor.routing.route

internal fun Application.main() {
    configureKtor()
    dataStoreInit()
    install(Routing) {
        route("api") {
            apiV1()
        }
    }
    if (isDev) {
        // https://ktor.io/servers/features/cors.html
        install(CORS) {
            host("localhost:4200")
            method(HttpMethod.Options)
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Put)
            method(HttpMethod.Delete)
            method(HttpMethod.Patch)
            header(HttpHeaders.AccessControlAllowHeaders)
            header(HttpHeaders.ContentType)
            header(HttpHeaders.AccessControlAllowOrigin)
            header(HttpHeaders.Authorization)
            allowCredentials = true
        }
    }
}
