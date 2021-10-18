package com.augdim.console.backend.api.v1

import com.augdim.backend.BadRequest
import com.augdim.backend.NotFound
import com.augdim.console.backend.model.Template
import com.augdim.console.backend.model.TemplateModel
import com.augdim.console.backend.model.View
import com.augdim.console.backend.model.ViewModel
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveParameters


fun Route.apiView() {
    route("views") {
        get {
            val views = ViewModel().getViews();
            call.respond(views);
        }
    }
    route("view") {
        route("{id}") {
            //View
            get {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequest()
                val view = ViewModel().get(id) ?: throw NotFound("view $id")
                call.respond(view)
            }
            delete {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequest()
                ViewModel().delete(id)
                call.response.status(HttpStatusCode.OK)
            }
            // Item
            route("{itemId}") {
                get {
                    val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequest()
                    val itemId = call.parameters["itemId"]?.toLongOrNull() ?: throw BadRequest()
                    val item = ViewModel().getItem(id, itemId) ?: throw NotFound("view $id, item $itemId")
                    call.respond(item)
                }
//                delete {
//                    val id = call.parameters["id"]?.toLongOrNull() ?: throw BadRequest()
//                    val itemId = call.parameters["itemId"]?.toLongOrNull() ?: throw BadRequest()
//                    ViewModel().deleteItem(id, itemId)
//                    call.response.status(HttpStatusCode.OK)
//                }
            }
        }
        // View
        post {
            val params = call.receiveParameters()
            val view = View(0L,
                        params["name"] ?: throw BadRequest(),
                        params["title"] ?: throw BadRequest(),
                        params["templateId"]?.toLong() ?: throw BadRequest()
                        )
            val result = ViewModel().add(view)
            if (result == -1L) {
                call.response.status(HttpStatusCode.ExpectationFailed)
            }
            else {
                call.respond(result)
            }
        }
        put {
            val params = call.receiveParameters()
            val view = View(params["id"]?.toLong() ?: throw BadRequest(),
                    params["name"] ?: throw BadRequest(),
                    params["title"] ?: throw BadRequest(),
                    params["templateId"]?.toLong() ?: throw BadRequest())
            val result = ViewModel().update(view)
            call.response.status(if (result) HttpStatusCode.OK else HttpStatusCode.NotFound)
        }
    }
}

