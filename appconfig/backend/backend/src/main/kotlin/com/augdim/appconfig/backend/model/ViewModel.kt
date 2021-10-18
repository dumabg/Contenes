package com.augdim.appconfig.backend.model

import com.augdim.backend.gcd.DatastoreService
import com.augdim.backend.gcd.EntityAdapter
import com.google.cloud.datastore.Query

class View(val id: Long,
           val name: String,
           val templateId: Long,
           val title: String,
           var accessPoint: Long? = null)

class ViewModel: DatastoreService() {

    fun getViews(): List<View> {
        val result = mutableListOf<View>()
        val kind = EntityAdapter.kind(View::class)
        val queryBuilder = Query.newEntityQueryBuilder().setKind(kind)
//        val queryBuilder = Query.newProjectionEntityQueryBuilder()
//                .setKind(kind)
//                .setProjection(View::name.name, View::templateId.name, View::accessPoint.name)
        val queryResult = datastore.run(queryBuilder.build())
        while (queryResult.hasNext()) {
            val entity = queryResult.next()
            val view = EntityAdapter.fromEntity<View>(entity)
            result.add(view)
        }
        return result
    }
}