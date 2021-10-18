package com.augdim.console.backend.model

import com.augdim.app.backend.gae.Group
import com.augdim.app.backend.gae.Item
import com.augdim.backend.gcd.EntityAdapter
import com.google.cloud.datastore.*
import io.kotlintest.specs.StringSpec
import io.kotlintest.*
import io.kotlintest.data.forall
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.tables.row

class EntityAdapterTest : StringSpec() {

    private lateinit var datastore: Datastore
    private lateinit var keyFactoryGroup: KeyFactory
    private lateinit var keyFactoryItem: KeyFactory

    override fun beforeTest(testCase: TestCase) {
        datastore = DatastoreOptions.getDefaultInstance().service
        keyFactoryGroup = datastore.newKeyFactory().setKind("Group")
        keyFactoryItem = datastore.newKeyFactory().setKind("Item")
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
    }

    init {
        "fromEntity Group" {
            EntityAdapter.register(Group::id) {
                ignore(Group::items)
            }
            EntityAdapter.register(Item::id)
            forall(
                    row({
                        val key = keyFactoryGroup.newKey(1L)
                        val entity = Entity.newBuilder(key)
                                .set("title", "Title 1")
                                .set("description", "")
                                .build()
                        entity
                    }, Group(1L, "Title 1")),
                    row({
                        val key = keyFactoryGroup.newKey(2L)
                        val entity = Entity.newBuilder(key)
                                .set("title", "Title 2")
                                .set("description", "")
                                .build()
                        entity
                    }, Group(2L, "Title 2"))
            ) { entity, group ->
                val groupCreated = EntityAdapter.fromEntity<Group>(entity())
                println(group)
                println(groupCreated)
                groupCreated.id.shouldBe(group.id)
                groupCreated.title.shouldBe(group.title)
            }
        }


        "fromEntity Item" {
            EntityAdapter.register(Item::id)
            forall(
                    row({
                        val key = keyFactoryItem.newKey("id1")
                        val entity = Entity.newBuilder(key)
                                .set("title", "Title 1")
                                .build()
                        entity
                    }, Item("id1", "Title 1"))
            ) { entity, item ->
                val itemCreated = EntityAdapter.fromEntity<Item>(entity())
                println(item)
                println(itemCreated)
                itemCreated.id.shouldBe(item.id)
                itemCreated.title.shouldBe(item.title)
            }
        }

        "Convert" {
            EntityAdapter.register(Group::id) {
                ignore(Group::items)
                convert(Group::description,
                        { value ->
                            BooleanValue.newBuilder(value == "Si") },
                        { value ->
                            if (value.get()) "Si" else "No" })
            }
            forall(
                    row({
                        val key = keyFactoryGroup.newKey(1L)
                        val entity = Entity.newBuilder(key)
                                .set("title", "Title 1")
                                .set("description", true)
                                .build()
                        entity
                    }, {
                        val group = Group(1L, "Title 1")
                        group.description = "Si"
                        group
                    }),
                    row({
                        val key = keyFactoryGroup.newKey(2L)
                        val entity = Entity.newBuilder(key)
                                .set("title", "Title 2")
                                .set("description", false)
                                .build()
                        entity
                    }, {
                        val group = Group(2L, "Title 2")
                        group.description = "No"
                        group
                    })
            ) { entityLambda, groupLambda ->
                val group = groupLambda()
                val groupCreated = EntityAdapter.fromEntity<Group>(entityLambda())
                println(group)
                println(groupCreated)
                groupCreated.id.shouldBe(group.id)
                groupCreated.title.shouldBe(group.title)
                groupCreated.description.shouldBeTypeOf<String>()
                groupCreated.description.shouldBe(group.description)
            }
        }
    }
}

//        "toEntity" {
//            EntityAdapter.register(Group::id, 0) {
//
//            }
////            EntityAdapter.register<Group> {
////                id(Group::id)
////                name("Grupo")
////                ignore(Group::items)
////                //rename(Group::title, "titulo")
////            }
//            EntityAdapter.fromEntity<Group>(entity)
//            val entity = Entity("Grupo", 2L)
//            entity.setProperty("title", "Segundo titulo")
//
//        }
//    }

