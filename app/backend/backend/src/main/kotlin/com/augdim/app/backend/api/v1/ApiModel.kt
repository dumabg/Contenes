package com.augdim.app.backend.api.v1

enum class ItemType { TEXT, HTTP }

open class Item (
        val id: Long,
        val position: Int,
        val text: String,
        val type: Int = ItemType.TEXT.ordinal
        )

class ItemHttp(id: Long,
               position: Int,
               text: String,
               val url: String): Item(id, position, text, ItemType.HTTP.ordinal)

class View(val id: Long,
           val templateId: Long,
           val title: String,
           val items: MutableList<Item> = mutableListOf())
