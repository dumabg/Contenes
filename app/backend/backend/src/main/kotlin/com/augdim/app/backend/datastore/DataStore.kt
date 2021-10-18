package com.augdim.app.backend.datastore

import com.augdim.app.backend.api.v1.*
import com.augdim.backend.gcd.EntityAdapter

fun dataStoreInit() {
    EntityAdapter.register(View::id)
    EntityAdapter.register(Item::id) {
        selector(ItemHttp::class) { it.properties.containsKey("url") }
        selector(Item::class) { true }
    }
    EntityAdapter.register(ItemHttp::id)
}

