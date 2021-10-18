package com.augdim.console.backend.model

import com.augdim.backend.gcd.DefaultDataStore
import com.augdim.backend.gcd.EntityAdapter
import com.google.cloud.datastore.Query

open class TemplateId(val id: Long, val name: String)

class Template(id: Long, name: String, val items: MutableList<Item> = mutableListOf()): TemplateId(id, name)

class TemplateModel : DefaultDataStore<Template>(Template::class) {

    fun getTemplates(): List<TemplateId> {
        val result = mutableListOf<TemplateId>()
        val kind = EntityAdapter.kind(TemplateId::class)
        val query = Query.newProjectionEntityQueryBuilder()
                .setKind(kind)
                .setProjection(TemplateId::name.name)
                .build()
        val queryResult = datastore.run(query)
        while (queryResult.hasNext()) {
            val entity = queryResult.next()
            val template = EntityAdapter.fromEntity<TemplateId>(entity)
            result.add(template)
        }
        return result
    }

    override fun get(id: Long): Template? {
        val template = super.get(id) ?: return null
        var keyTemplate = key(Template::class, id)
        val items = ItemsModel().get(keyTemplate)
        template.items.addAll(items)
        return template
    }

    override fun delete(id: Long) {
        val transaction = datastore.newTransaction()
        var keyTemplate = key(Template::class, id)
        val keys = ItemsModel().getKeys(keyTemplate)
        keys.add(keyTemplate)
        datastore.delete(*keys.toTypedArray())
        transaction.commit()
    }

    fun getItem(idTemplate: Long, idItem: Long): Item? {
        return ItemsModel().getItem("Template", idTemplate, idItem)
    }

    fun addItem(idTemplate: Long, item: Item): Long {
        val keyTemplate = key(Template::class, idTemplate)
        return ItemsModel().addItem(keyTemplate, item)
    }

    fun updateItem(idTemplate: Long, item: Item): Boolean {
        val keyTemplate = key(Template::class, idTemplate)
        return ItemsModel().updateItem(keyTemplate, item)
    }

    fun updateItemsOrder(idTemplate: Long, idItem1: Long, position1: Int, idItem2: Long, position2: Int): Boolean {
        return ItemsModel().updateItemsOrder(idTemplate, idItem1, position1, idItem2, position2)
    }

    fun deleteItem(idTemplate: Long, idItem: Long) {
        ItemsModel().deleteItem("Template", idTemplate, idItem)
    }
}
