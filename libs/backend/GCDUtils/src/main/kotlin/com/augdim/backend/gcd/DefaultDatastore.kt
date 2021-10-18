package com.augdim.backend.gcd

import com.augdim.backend.datastore.DataStore
import kotlin.reflect.KClass

open class DefaultDataStore<T>(private val clazz: KClass<*>): DatastoreService(), DataStore<T> {

    override fun update(obj: T): Boolean {
        return updateEntity(obj!!)
    }

    override fun delete(id: Long) {
        deleteEntity(clazz, id)
    }

    override fun get(id: Long): T? {
        return getEntity(clazz, id)
    }

    override fun add(obj: T): Long {
        val entity = putEntity(obj!!)
        return entity.key.id
    }
}