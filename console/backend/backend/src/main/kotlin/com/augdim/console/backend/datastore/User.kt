package com.augdim.console.backend.datastore

open class UserWithoutId(val name: String, val password: String)

class User(val id: Long, name: String, password: String): UserWithoutId(name, password)
