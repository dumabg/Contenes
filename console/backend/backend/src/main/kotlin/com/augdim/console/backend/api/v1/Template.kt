package com.augdim.console.backend.api.v1

import com.augdim.backend.BadRequest
import com.augdim.backend.NotFound
import com.augdim.console.backend.model.Template
import com.augdim.console.backend.model.TemplateModel
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.apiTemplate() {
    route("templates") {
        get {
            val templates = TemplateModel().getTemplates()
            call.respond(templates)
        }
    }
    route("template") {
        route("{id}") {
            get {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequest()
                val template = TemplateModel().get(id) ?: throw NotFound("template $id")
                call.respond(template)
            }
            delete {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequest()
                TemplateModel().delete(id)
                call.response.status(HttpStatusCode.OK)
            }
            // Item
            route("{itemId}") {
                get {
                    val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequest()
                    val itemId = call.parameters["itemId"]?.toLongOrNull() ?: throw BadRequest()
                    val item = TemplateModel().getItem(id, itemId) ?: throw NotFound("view $id, item $itemId")
                    call.respond(item)
                }
                delete {
                    val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequest()
                    val itemId = call.parameters["itemId"]?.toLongOrNull() ?: throw BadRequest()
                    TemplateModel().deleteItem(id, itemId)
                    call.response.status(HttpStatusCode.OK)
                }
            }
            post {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequest()
                val item = getItem(call.receiveParameters(), true)
                val idItem = TemplateModel().addItem(id, item)
                call.respond(idItem)
            }
            put {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequest()
                val params = call.receiveParameters()
                val updateType = params["t"]
                val result: Boolean = if (updateType == null) {
                        val item = getItem(params, false)
                        TemplateModel().updateItem(id, item)
                    }
                    else {
                        when(updateType) {
                            "order" -> TemplateModel().updateItemsOrder(id,
                                    params["id1"]?.toLong() ?: throw BadRequest(),
                                    params["pos1"]?.toInt() ?: throw BadRequest(),
                                    params["id2"]?.toLong() ?: throw BadRequest(),
                                    params["pos2"]?.toInt() ?: throw BadRequest())
                            else -> throw BadRequest()
                        }
                    }
                call.response.status(if (result) HttpStatusCode.OK else HttpStatusCode.NotFound)
            }
        }
        post {
            val params = call.receiveParameters()
            val template = Template(0L,
                    params["name"] ?: throw BadRequest())
            val result = TemplateModel().add(template)
            call.respond(result)
        }
        put {
            val params = call.receiveParameters()
            val template = Template(params["id"]?.toLong() ?: throw BadRequest(),
                                params["name"] ?: throw BadRequest())
            val result = TemplateModel().update(template)
            call.response.status(if (result) HttpStatusCode.OK else HttpStatusCode.NotFound)
        }
    }
}