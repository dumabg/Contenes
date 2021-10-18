package com.augdim.console.backend.model

import com.augdim.console.backend.datastore.RegisterDataStore
import com.augdim.console.backend.datastore.User
import com.augdim.console.backend.datastore.UserWithoutId

class UserAlreadyExistsException: Throwable()

class GaeRegisterDataStore : RegisterDataStore() {

    override fun add(obj: UserWithoutId): Long {
        if (exists(User::name, obj.name)) {
            throw UserAlreadyExistsException()
        }
        else {
            var entity = putEntity(obj)
            return entity.key.id
        }
    }
}
