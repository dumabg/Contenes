package com.augdim.console.backend.model

import com.augdim.backend.gcd.DatastoreService
import com.augdim.console.backend.datastore.ILoginDataStore
import com.augdim.console.backend.datastore.User


class LoginDataStore : DatastoreService(), ILoginDataStore {

    override fun get(user: String): User? {
        return getEntity(User::name, user)
    }
}
