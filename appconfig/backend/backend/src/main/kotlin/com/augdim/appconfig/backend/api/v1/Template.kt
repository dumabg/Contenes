package com.augdim.appconfig.backend.api.v1

import com.augdim.appconfig.backend.model.TemplateModel
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.apiTemplate() {
    route("templates") {
        get {
            val templates = TemplateModel().getTemplates()

            call.respond(templates)
        }
    }
}