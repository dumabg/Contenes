package com.augdim.appconfig.backend.model

import com.augdim.backend.gcd.DatastoreService
import com.augdim.appconfig.backend.datastore.User


class LoginModel : DatastoreService() {

    fun get(user: String): User? {
        return getEntity(User::name, user)
    }
}
