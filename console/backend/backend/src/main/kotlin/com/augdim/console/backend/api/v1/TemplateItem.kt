package com.augdim.console.backend.api.v1

import com.augdim.backend.BadRequest
import com.augdim.console.backend.model.Item
import com.augdim.console.backend.model.ItemHttp
import com.augdim.console.backend.model.ItemText
import io.ktor.http.Parameters

 fun getItem(params: Parameters, isNew: Boolean): Item {
    val type = params["type"]?.toInt() ?: throw BadRequest()
     val id = if (isNew) 0L else params["id"]?.toLong() ?: throw BadRequest()
    when (type) {
        0 -> return ItemText(
                id,
                params["position"]?.toInt() ?: throw BadRequest(),
                params["text"] ?: throw BadRequest()
        )
        1 -> return ItemHttp(
                id,
                params["position"]?.toInt() ?: throw BadRequest(),
                params["text"] ?: throw BadRequest(),
                    params["url"] ?: throw BadRequest()
                )
    }
    throw BadRequest()
}