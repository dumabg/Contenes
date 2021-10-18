package com.augdim.console.backend.model

import com.augdim.backend.gcd.DatastoreService

class CodeDataStore: DatastoreService() {

    fun createCodes(appId: Long, num: Int): List<String> {
        val keyFactory = datastore.newKeyFactory().setKind("Code")
        val incompleteKeys = Array(num) { keyFactory.newKey() }
        val keys = datastore.allocateId(*incompleteKeys)
        val prefix = "https://m4rn3.app.goo.gl/YM0b?i=$appId#"
        return List(keys.size) {i -> "$prefix${keys[i].id}"}
//        val codes = mutableListOf<String>()
//        for (key in keys) {
//            codes.add("$prefix$key.id")
//        }
//        return codes
    }
}