package com.augdim.app.backend

import com.augdim.app.backend.api.v1.api
import com.augdim.app.backend.datastore.dataStoreInit
import com.augdim.backend.configureKtor
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.routing.Routing

internal fun Application.main() {
    configureKtor()
    dataStoreInit()
    install(Routing) {
        api()
    }
}
