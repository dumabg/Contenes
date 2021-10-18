package com.augdim.appconfig.backend.model

import com.augdim.backend.gcd.DatastoreService
import com.augdim.backend.gcd.EntityAdapter
import com.google.cloud.datastore.Query

class Template(val id: Long, val name: String)

class TemplateModel : DatastoreService() {

    fun getTemplates(): List<Template> {
        val result = mutableListOf<Template>()
        val kind = EntityAdapter.kind(Template::class)
        val query = Query.newProjectionEntityQueryBuilder()
                .setKind(kind)
                .setProjection(Template::name.name)
                .build()
        val queryResult = datastore.run(query)
        while (queryResult.hasNext()) {
            val entity = queryResult.next()
            val template = EntityAdapter.fromEntity<Template>(entity)
            result.add(template)
        }
        return result
    }
}
