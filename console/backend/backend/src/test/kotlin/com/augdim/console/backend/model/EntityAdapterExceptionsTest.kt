package com.augdim.console.backend.model

import com.augdim.app.backend.gae.Group
import com.augdim.backend.gcd.*
import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.KeyFactory
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec

class EntityAdapterExceptionsTest : StringSpec() {
//    private val helper = LocalServiceTestHelper(LocalDatastoreServiceTestConfig())
    private lateinit var datastore: Datastore
    private lateinit var keyFactory: KeyFactory

    override fun beforeTest(testCase: TestCase) {
//        helper.setUp()
        datastore = DatastoreOptions.getDefaultInstance().service
        keyFactory = datastore.newKeyFactory().setKind("Group")
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
//        helper.tearDown()
    }

    init {
        "ClassNotRegisteredException for class not registered" {
            class NoRegisteredClass
            val instance = NoRegisteredClass()
            val exception = shouldThrow<ClassNotRegisteredException> {
                EntityAdapter.toFullEntity(instance)
            }
            println(exception)
        }

        "ClassMustBePublicException for class not public" {
            class NoPublicClass
            val exception = shouldThrow<ClassMustBePublicException> {
                EntityAdapter.register<NoPublicClass>()
            }
            println(exception)
        }

        "ClassNotFoundForEntityException" {
            val key = keyFactory.newKey(1L)
            val entity = Entity.newBuilder(key).build()
            val exception = shouldThrow<ClassNotFoundForEntityException> {
                EntityAdapter.fromEntity<Group>(entity)
            }
            println(exception)
        }

        "CantConstructClassFromEntityException" {
            EntityAdapter.register(Group::id) {
                ignore(Group::items)
            }
            val key = keyFactory.newKey(1L)
            val entity = Entity.newBuilder(key).build()
            val exception = shouldThrow<CantConstructClassFromEntityException> {
                EntityAdapter.fromEntity<Group>(entity)
            }
            println(exception)
        }
    }
}