package com.augdim.appconfig.backend

import com.augdim.appconfig.backend.api.v1.api
import com.augdim.appconfig.backend.datastore.dataStoreInit
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
