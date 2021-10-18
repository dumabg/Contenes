package com.augdim.app.backend.api.v1

import com.augdim.app.backend.isDev
import io.ktor.routing.Routing
import io.ktor.routing.route

internal fun Routing.api() {
    route("v1") {
        apiView()
        apiItem()
    }
}

