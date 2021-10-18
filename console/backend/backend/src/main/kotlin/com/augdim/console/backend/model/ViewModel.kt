package com.augdim.console.backend.model

import com.augdim.backend.gcd.DefaultDataStore
import com.augdim.backend.gcd.EntityAdapter
import com.google.cloud.datastore.Query

class View(val id: Long,
           val name: String,
           val title: String,
           val templateId: Long)

class ViewModel: DefaultDataStore<View>(View::class) {

//    override fun add(obj: View): Long {
//        val transaction = datastore.newTransaction()
//        val entity = putEntity(obj!!)
//        val key = entity.key
//        val itemsEntities = EntityAdapter.toFullEntityList(datastore, obj.items, key)
//        datastore.put(*itemsEntities.toTypedArray())
//        transaction.commit()
//        return key.id
//    }

    fun getViews(): List<View> {
        val result = mutableListOf<View>()
        val kind = EntityAdapter.kind(View::class)
        val query = Query.newEntityQueryBuilder()
                .setKind(kind)
                .build()
        val queryResult = datastore.run(query)
        while (queryResult.hasNext()) {
            val entity = queryResult.next()
            val view = EntityAdapter.fromEntity<View>(entity)
            result.add(view)
        }
        return result
    }

    override fun get(id: Long): View? {
        val view = super.get(id) ?: return null
//        var keyView = key(View::class, id)
//        val items = ItemsModel().get(keyView)
//        view.items.addAll(items)
        return view
    }

    override fun delete(id: Long) {
        val transaction = datastore.newTransaction()
        var keyView = key(View::class, id)
        val keys = ItemsModel().getKeys(keyView)
        keys.add(keyView)
        datastore.delete(*keys.toTypedArray())
        transaction.commit()
    }

    override fun add(obj: View): Long {
        val idTemplate = obj.templateId
        if (!exists<Template>(idTemplate)) {
            return -1L
        }
        return super.add(obj)
    }

    fun getItem(idView: Long, idItem: Long): Item? {
         return ItemsModel().getItem("View", idView, idItem)
    }

//    fun addItem(idView: Long, item: Item): Long {
//        val keyView = key(View::class, idView)
//        return ItemsModel().addItem(keyView, item)
//    }
//
//    fun updateItem(idView: Long, item: Item): Boolean {
//        val keyView = key(View::class, idView)
//        return ItemsModel().updateItem(keyView, item)
//    }

//    fun deleteItem(idView: Long, idItem: Long) {
//        ItemsModel().deleteItem("View", idView, idItem)
//    }
}
