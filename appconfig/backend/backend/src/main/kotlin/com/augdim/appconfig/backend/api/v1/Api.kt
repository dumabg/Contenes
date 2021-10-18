package com.augdim.appconfig.backend.api.v1

import io.ktor.routing.Routing
import io.ktor.routing.route
import com.augdim.backend.authenticateGae

internal fun Routing.api() {
    route("v1") {
        apiLogin()
        authenticateGae {
            apiTemplate()
            apiView()
            accessPoint()
        }
    }
}

