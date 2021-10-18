package com.augdim.console.backend.datastore

import com.augdim.backend.datastore.DataStoreAdd
import com.augdim.backend.gcd.DatastoreService
import com.augdim.backend.gcd.EntityAdapter
import com.augdim.console.backend.model.*

fun dataStoreInit() {
    EntityAdapter.defaultIdNullLong = 0L
    EntityAdapter.register(View::id) {
        indexed(View::templateId) //appconfig makes a filter (ViewModel.getViews)
                                  //app makes a filter and a projection (ViewDataStore.get)
    }
    EntityAdapter.register(Item::id) {
        selector(ItemHttp::class) { it.properties.containsKey("url") }
        selector(ItemText::class) { true }
    }
    EntityAdapter.register(ItemText::id) {
        kind("Item")
        ignore(ItemText::type)
    }
    EntityAdapter.register(ItemHttp::id) {
        kind("Item")
        ignore(ItemHttp::type)
    }
    EntityAdapter.register(Template::id) {
        indexed(Template::name) //is required because this and appconfig makes a projection query with this property
    }
    EntityAdapter.register(TemplateId::id) {
        kind("Template")
    }
    EntityAdapter.register(User::id)
    EntityAdapter.register<UserWithoutId> {
        kind("User")
        indexed(UserWithoutId::name)
    }
}
//
//fun dataStoreGroup(): DataStoreGroup = GaeDataStoreGroup()
//fun dataStoreGroupItem(): DataStoreGroupItem = GaeDataStoreGroupItem()

abstract class RegisterDataStore: DatastoreService(), DataStoreAdd<UserWithoutId>
fun registerDataStore(): RegisterDataStore = GaeRegisterDataStore()

interface ILoginDataStore {
    fun get(user: String): User?
}
fun loginDataStore(): ILoginDataStore = LoginDataStore()

fun codeDataStore(): CodeDataStore = CodeDataStore()


