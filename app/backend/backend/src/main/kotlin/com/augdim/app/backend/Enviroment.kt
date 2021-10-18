package com.augdim.app.backend

import com.google.appengine.api.utils.SystemProperty

// Detect dev environment with Google app engine
fun isDev() = SystemProperty.environment.value() == SystemProperty.Environment.Value.Development // Production

// Detect dev environment with ktor configuration. The ktor.environment property is configured in application.conf
//val Application.envKind get() = environment.config.property("ktor.environment").getString()
//val Application.isDev get() = envKind == "dev"
//val Application.isProd get() = envKind != "dev"