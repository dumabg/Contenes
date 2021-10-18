package com.augdim.console.backend.model

import com.augdim.app.backend.gae.Group
import com.augdim.app.backend.gae.Item
import com.augdim.backend.gcd.EntityAdapter
import com.google.cloud.datastore.*
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.data.forall
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class EntityAdapterToEntityTest : StringSpec() {

//    private val helper = LocalServiceTestHelper(LocalDatastoreServiceTestConfig())
    private lateinit var datastore: Datastore
    private lateinit var keyFactory: KeyFactory
    private lateinit var keyFactoryGrupo: KeyFactory


    override fun beforeTest(testCase: TestCase) {
        //helper.setUp()
        datastore = DatastoreOptions.getDefaultInstance().service
        keyFactory = datastore.newKeyFactory().setKind("Group")
        keyFactoryGrupo = datastore.newKeyFactory().setKind("Grupo")
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        //helper.tearDown()
    }

    init {
        "Indexed" {
            EntityAdapter.register(Group::id) {
                ignore(Group::items)
                indexed(Group::title)
            }
            forall(
                    row(Group(1L, "Title 1"),
                        FullEntity.newBuilder(keyFactory.newKey(1L))
                                .set("title", StringValue.newBuilder("Title 1").setExcludeFromIndexes(false).build())
                                .set("description", StringValue.newBuilder("").setExcludeFromIndexes(true).build())
                                .build()
                    ),
                    row(Group(2L, "Title 2"),
                        FullEntity.newBuilder(keyFactory.newKey(2L))
                                .set("description", StringValue.newBuilder("").setExcludeFromIndexes(true).build())
                                .set("title", StringValue.newBuilder("Title 2").setExcludeFromIndexes(false).build())
                                .build()
                    )
            ) { group, entity ->
                val entityAdapted = EntityAdapter.toFullEntity(group)
                println(entityAdapted)
                println(entity)
                entity.properties.shouldBe(entityAdapted.properties)
                entity.key.kind.shouldBe(entityAdapted.key.kind)
                entity.key.id.shouldBe((entityAdapted.key as Key).id)
            }
        }

        "Ignore" {
            EntityAdapter.register(Group::id) {
                ignore(Group::items)
                indexed(Group::title)
                indexed(Group::description)
            }
            forall(
                    row(Group(1L, "Title 1"),
                            FullEntity.newBuilder(keyFactory.newKey(1L))
                                    .set("title", "Title 1")
                                    .set("description","")
                                    .build()
                    ),
                    row(Group(2L, "Title 2"),
                            FullEntity.newBuilder(keyFactory.newKey(2L))
                                    .set("description", "")
                                    .set("title", "Title 2")
                                    .build()
                    )
            ) { group, entity ->
                val entityAdapted = EntityAdapter.toFullEntity(group)
                println(entityAdapted)
                println(entity)
                entity.properties.shouldBe(entityAdapted.properties)
                entity.key.kind.shouldBe(entityAdapted.key.kind)
                entity.key.id.shouldBe((entityAdapted.key as Key).id)
            }
        }

        "Rename and kind" {
            EntityAdapter.register(Group::id) {
                kind("Grupo")
                ignore(Group::items)
                ignore(Group::description)
                rename(Group::title, "titulo")
                indexed(Group::title)
            }
            forall(
                    row(Group(1L, "Title 1"),
                        FullEntity.newBuilder(keyFactoryGrupo.newKey(1L))
                                .set("titulo", "Title 1")
                                .build()
                    ),
                    row(Group(2L, "Title 2"),
                        FullEntity.newBuilder(keyFactoryGrupo.newKey(2L))
                                .set("titulo", "Title 2")
                                .build()
                    )
            ) { group, entity ->
                val entityAdapted = EntityAdapter.toFullEntity(group)
                println(entityAdapted)
                println(entity)
                entity.properties.shouldBe(entityAdapted.properties)
                entity.key.kind.shouldBe(entityAdapted.key.kind)
                entity.key.id.shouldBe((entityAdapted.key as Key).id)
            }
        }

        "Convert" {
            EntityAdapter.register(Group::id) {
                ignore(Group::items)
                indexed(Group::description)
                convert(Group::description,
                        { value -> BooleanValue.newBuilder(value == "1") } ,
                        { value -> value.get()})
            }
            forall(
                    row({val group = Group(1L, "Title 1")
                        group.description = "1"
                        group
                    } , {
                        val key = keyFactory.newKey(1L)
                        val entity = FullEntity.newBuilder(key)
                                .set("description", true)
                                .build()
                        entity
                    }),
                    row({
                        val group = Group(2L, "Title 2")
                        group.description = "0"
                        group
                    }, {
                        val key = keyFactory.newKey(2L)
                        val entity = FullEntity.newBuilder(key)
                                .set("description", false)
                                .build()
                        entity
                    })
            ) { groupLambda, entityLambda ->
                val entityAdapted = EntityAdapter.toFullEntity(groupLambda())
                println(entityAdapted)
                val entity = entityLambda()
                println(entity)
                entity.key.kind.shouldBe(entityAdapted.key.kind)
                entity.key.id.shouldBe((entityAdapted.key as Key).id)
                entityAdapted.properties["description"].shouldBeTypeOf<BooleanValue>()
            }
        }

        "EntityList" {
            EntityAdapter.register(Item::id) {
                indexed(Item::title)
            }
            val items = listOf(Item("id1", "title item 1"),
                               Item("id2", "title item 2"))
            val entities = EntityAdapter.toFullEntityList(items)
            println(items)
            println(entities)
            entities.size.shouldBe(items.size)

            val keyFactoryItem = datastore.newKeyFactory().setKind("Item")
            var key = keyFactoryItem.newKey("id1")
            val entity1 = FullEntity.newBuilder(key)
                    .set("title", "title item 1")
                    .build()
            entities[0].shouldBe(entity1)
            key = keyFactoryItem.newKey("id2")
            val entity2 = FullEntity.newBuilder(key)
                    .set("title", "title item 2")
                    .build()
            entities[1].shouldBe(entity2)
        }
    }

}