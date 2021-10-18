package com.augdim.app.backend.gae

data class Item(val id: String, var title: String)

data class Group(val id: Long,
                 val title: String,
                 val items: List<Item> = listOf()) {
    var description = ""
    constructor(id: Long, title: String) : this(id, title, listOf())
    constructor(title: String, items: List<Item>) : this(0, title, items)
//    constructor(): this(0, "", listOf())
}
