package com.augdim.app.backend.model

import com.augdim.app.backend.api.v1.Item
import com.augdim.app.backend.api.v1.ItemHttp
import com.augdim.backend.BadRequest
import com.augdim.backend.gcd.DefaultDataStore
import com.google.cloud.datastore.PathElement

class ItemModel: DefaultDataStore<Item>(Item::class) {

    suspend fun execute(templateId: Long, itemId: Long): String {
        val key = datastore.newKeyFactory().addAncestor(PathElement.of("Template", templateId)).setKind("Item").newKey(itemId)
        val item = getEntity<Item>(key)
        return when (item) {
            is ItemHttp -> {ItemHttpModel().execute(item.url)}
            else -> throw BadRequest()
        }
    }
}