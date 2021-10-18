package com.augdim.backend

import com.google.appengine.api.utils.SystemProperty
import io.ktor.application.Application
//import io.ktor.util.KtorExperimentalAPI

// Detect dev environment with Google app engine
val Application.isDev get() = SystemProperty.environment.value() == SystemProperty.Environment.Value.Development // Production

// Detect dev environment with ktor configuration. The ktor.environment property is configured in application.conf
//@UseExperimental(KtorExperimentalAPI::class)
//val Application.envKind get() = environment.config.property("ktor.environment").getString()
//val Application.isDev get() = envKind == "dev"
//val Application.isProd get() = envKind != "dev"