package com.augdim.appconfig.backend.datastore

import com.augdim.appconfig.backend.model.Template
import com.augdim.appconfig.backend.model.View
import com.augdim.backend.gcd.EntityAdapter

fun dataStoreInit() {
    EntityAdapter.defaultIdNullLong = 0L
    EntityAdapter.register(User::id)
    EntityAdapter.register(View::id) {
        indexed(View::accessPoint)
        indexed(View::templateId)
    }
    EntityAdapter.register(Template::id)
    //EntityAdapter.register(AccessPoint::id)
}
