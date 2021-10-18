package com.augdim.app.backend.model

import io.ktor.client.HttpClient
import io.ktor.client.request.get

class ItemHttpModel {

    suspend fun execute(url: String): String {
        val client = HttpClient()
        val result = client.get<String>(url)
        client.close()
        return result
    }
}