package com.augdim.backend.datastore

interface DataStoreAdd<T> {
    fun add(obj: T): Long
}

interface DataStoreGet<T> {
    fun get(id: Long): T?
}

interface DataStoreUpdate<T> {
    fun update(obj: T): Boolean
}

interface DataStoreDelete<T> {
    fun delete(id: Long)
}

interface DataStore<T>: DataStoreAdd<T>, DataStoreGet<T>, DataStoreUpdate<T>, DataStoreDelete<T>
