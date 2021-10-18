package com.augdim.app.backend.model

import com.augdim.app.backend.api.v1.Item
import com.augdim.app.backend.api.v1.ItemHttp
import com.augdim.app.backend.api.v1.ItemType
import com.augdim.app.backend.api.v1.View
import com.augdim.backend.NotFound
import com.augdim.backend.gcd.DefaultDataStore
import com.augdim.backend.gcd.EntityAdapter
import com.google.cloud.datastore.LongValue
import com.google.cloud.datastore.Query
import com.google.cloud.datastore.StructuredQuery

class ViewModel: DefaultDataStore<View>(View::class) {

    fun getView(accessPoint: Long): View? {
        val entity = getEntity(View::class, "accessPoint", LongValue(accessPoint))
        if (entity != null) {
            val templateId = entity.getLong("templateId")
            var keyTemplate = datastore.newKeyFactory().setKind("Template").newKey(templateId)
            val queryItems = Query.newEntityQueryBuilder().setKind("Item").setFilter(
                    StructuredQuery.PropertyFilter.hasAncestor(keyTemplate)).build()
            val entityItems = datastore.run(queryItems)
            val view = View(entity.key.id, templateId, entity.getString("title"))
            for (entityItem in entityItems) {
                val item = EntityAdapter.fromEntity<Item>(entityItem)
                view.items.add(
                    when(item) {
                        is ItemHttp -> Item(item.id, item.position, item.text, ItemType.HTTP.ordinal)
                        else -> item
                    }
                )
            }
            return view
        }
        else {
            throw NotFound("AccessPoint " +  accessPoint + "not found")
        }


//        val queryView = Query.newProjectionEntityQueryBuilder().setKind("View").setFilter(
//                StructuredQuery.PropertyFilter.eq("accessPoint", accessPoint)).
//                setProjection("templateId").
//                build()
//        val result = datastore.run(queryView)
//        if (result.hasNext()) {
//            val entity = result.next()
//            val templateId = entity.getLong("templateId")
//            var keyTemplate = datastore.newKeyFactory().setKind("Template").newKey(templateId)
//            val queryItems = Query.newEntityQueryBuilder().setKind("Item").setFilter(
//                    StructuredQuery.PropertyFilter.hasAncestor(keyTemplate)).build()
//            val entityItems = datastore.run(queryItems)
//            val view = View(entity.key.id, templateId)
//            for (entityItem in entityItems) {
//                val item = EntityAdapter.fromEntity<Item>(entityItem)
//                view.items.add(
//                    when(item) {
//                        is ItemHttp -> Item(item.id, item.position, item.text, ItemType.HTTP.ordinal)
//                        else -> item
//                    }
//                )
//            }
//            return view
//        }
//        else {
//            throw NotFound("AccessPoint " +  accessPoint + "not found")
//        }
    }
}
