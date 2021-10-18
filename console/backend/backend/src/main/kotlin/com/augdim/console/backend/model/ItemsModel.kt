package com.augdim.console.backend.model

import com.augdim.backend.NotFound
import com.augdim.backend.gcd.DatastoreService
import com.augdim.backend.gcd.EntityAdapter
import com.google.cloud.datastore.Key
import com.google.cloud.datastore.PathElement
import com.google.cloud.datastore.Query
import com.google.cloud.datastore.StructuredQuery

open class Item (
        val id: Long,
        val type: Int,
        var position: Int
)

class ItemText(id: Long, position: Int, val text: String): Item(id, 0, position)

class ItemHttp(id: Long, position: Int, val text: String, val url: String): Item(id, 1, position)


class ItemsModel: DatastoreService() {

    fun get(ancestorKey: Key): List<Item>
    {
        val result = mutableListOf<Item>()
        val query = Query.newEntityQueryBuilder().setKind("Item").setFilter(
                StructuredQuery.PropertyFilter.hasAncestor(ancestorKey)).build()
        val entityItems = datastore.run(query)
        for (entityItem in entityItems) {
            val item = EntityAdapter.fromEntity<Item>(entityItem)
            result.add(item)
        }
        return result
    }

    fun getKeys(ancestorKey: Key): MutableList<Key> {
        val result = mutableListOf<Key>()
        val query = Query.newKeyQueryBuilder().setKind("Item").setFilter(
                StructuredQuery.PropertyFilter.hasAncestor(ancestorKey)).build()
        val keysItems = datastore.run(query)
        for (keyItem in keysItems) {
            result.add(keyItem)
        }
        return result
    }

    fun getItem(kindParent: String, idParent: Long, idItem: Long): Item? {
        val key = key(kindParent, idParent, idItem)
        return getEntity<Item>(key)
    }

    fun addItem(parentKey: Key, item: Item): Long {
        if (exists(parentKey)) {
            val fullEntity = EntityAdapter.toFullEntity(datastore, item, parentKey)
            return datastore.put(fullEntity).key.id
        }
        else {
            throw NotFound("$parentKey doesn't exist")
        }
    }

    fun updateItem(parentKey: Key, item: Item): Boolean {
        return updateEntity(item, parentKey)
    }

    fun deleteItem(kindParent: String, idParent: Long, idItem: Long) {
        val key = key(kindParent, idParent, idItem)
        datastore.delete(key)
    }

    private fun key(kindParent: String, idParent: Long, idItem: Long): Key = datastore.newKeyFactory().setKind("Item")
            .addAncestor(PathElement.of(kindParent, idParent))
            .newKey(idItem)

    fun updateItemsOrder(idTemplate: Long, idItem1: Long, position1: Int, idItem2: Long, position2: Int): Boolean {
        var result = false
        datastore.runInTransaction {
            val item1 = getItem("Template", idTemplate, idItem1) ?: throw NotFound("Template $idTemplate, item $idItem1")
            val item2 = getItem("Template", idTemplate, idItem2) ?: throw NotFound("Template $idTemplate, item $idItem2")
            item1.position = position1
            item2.position = position2

            val parentKey = key(Template::class, idTemplate)
            result = (updateItem(parentKey, item1)) && (updateItem(parentKey, item2))
        }
        return result
    }

}