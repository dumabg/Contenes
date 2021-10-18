package com.augdim.backend.gcd

import com.google.cloud.datastore.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

open class DatastoreService {

    protected val datastore: Datastore =  DatastoreOptions.getDefaultInstance().service ?: throw RuntimeException("Datastore service is null")

    protected fun key(clazz: KClass<*>, value: Long): Key {
        val kind = EntityAdapter.kind(clazz)
        return datastore.newKeyFactory().setKind(kind).newKey(value)
    }

    protected fun key(clazz: KClass<*>, value: String): Key {
        val kind = EntityAdapter.kind(clazz)
        return datastore.newKeyFactory().setKind(kind).newKey(value)
    }

    protected fun getEntity(clazz: KClass<*>, key: Key): Any? {
        val entity = datastore.get(key)
        return if (entity == null) {
            null
        }
        else {
            EntityAdapter.fromEntity(clazz, entity)
        }
    }

    protected inline fun <reified T> getEntity(key: Key): T? {
        return getEntity(T::class, key) as T?
    }

    protected inline fun <reified T> getEntity(id: Long): T? {
        return getEntity(T::class, id)
    }

    protected fun <T> getEntity(clazz: KClass<*>, id: Long): T? {
        val key = key(clazz, id)
        @Suppress("UNCHECKED_CAST")
        return getEntity(clazz, key) as T?
    }

    protected inline fun <reified T> getEntity(id: String): T? {
        return getEntity(T::class, id)
    }

    protected fun <T> getEntity(clazz: KClass<*>, id: String): T? {
        val key = key(clazz, id)
        @Suppress("UNCHECKED_CAST")
        return getEntity(clazz, key) as T?
    }


    protected fun getEntity(clazz: KClass<*>, name: String, value: Value<*>): Entity? {
        val query = Query.newEntityQueryBuilder()
                .setKind(EntityAdapter.kind(clazz))
                .setFilter(StructuredQuery.PropertyFilter.eq(name, value))
                .build()
        val result = datastore.run(query)
        return if (result.hasNext()) result.next() else null
    }

    protected inline fun <reified T, R> getEntity(property: KProperty1<T, R>, value: String): T? {
        val name = property.name
        val entity = getEntity(T::class, name, StringValue(value))
        return if (entity == null) null else EntityAdapter.fromEntity<T>(entity)
    }

    protected fun updateEntity(obj: Any, parent: Key? = null): Boolean {
        val entity = EntityAdapter.toEntity(datastore, obj, parent)
        return try {
            datastore.update(entity)
            true
        }
        catch(e: DatastoreException) {
            if (e.code == 5) { //NOT_FOUND
                false
            }
            else {
                throw e
            }
        }
    }

    protected fun putEntity(obj: Any, parent: Key? = null): Entity {
        val entity = EntityAdapter.toFullEntity(obj, parent)
        return datastore.put(entity)
    }

    protected fun deleteEntity(clazz: KClass<*>, id: Long) {
        val key = key(clazz, id)
        datastore.delete(key)
    }

    protected inline fun <reified T> deleteEntity(id: Long) {
        deleteEntity(T::class, id)
    }

    protected inline fun <reified T> deleteEntity(id: String) {
        val key = key(T::class, id)
        datastore.delete(key)
    }

    protected fun exists(key: Key): Boolean {
        val q = Query.newKeyQueryBuilder().setFilter(StructuredQuery.PropertyFilter.eq("__key__", key)).build()
        val result = datastore.run(q)
        return result.hasNext()
    }

    protected inline fun <reified T> exists(id: Long): Boolean {
        val key =  key(T::class, id)
        return exists(key)
    }

    protected fun exists(clazz: KClass<*>, name: String, value: Value<*>): Boolean {
        val kind = EntityAdapter.kind(clazz)
        val q = Query.newKeyQueryBuilder()
                .setKind(kind)
                .setFilter(StructuredQuery.PropertyFilter.eq(name, value)).build()
        val result = datastore.run(q)
        return result.hasNext()
    }

    protected inline fun <reified T, R> exists(property: KProperty1<T, R>, value: String): Boolean {
        val name = property.name
        return exists(T::class, name, StringValue(value))
    }
}