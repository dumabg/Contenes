package com.augdim.backend.gcd

import kotlin.reflect.*
import kotlin.reflect.full.memberProperties
import com.google.cloud.Timestamp
import com.google.cloud.datastore.*
import java.io.InputStream
import java.nio.ByteBuffer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import kotlin.reflect.full.isSubclassOf

class EntityAdapter {

    companion object {
        var defaultIdNullLong = 0L

        internal class Property(
            val nameInClass: String,
            var nameInEntity: String,
            val getter: KProperty1.Getter<*, Any?>,
            val setter: KMutableProperty.Setter<out Any?>?,
            var getConverter: (Any) -> ValueBuilder<*, *, *>,
            var setConverter: (Value<*>) -> Any,
            var indexed: Boolean
        )

        internal class Selector(val clazz: KClass<*>, val selector: (BaseEntity<Key>) -> Boolean)

        private class ConstructorStrategy(
            val constructor: KFunction<Any>,
            val constructorParameters: Array<KParameter?>,
            var table: BooleanArray
        )

        private class MetaInfo(val clazz: KClass<*>,
                               val entityKind: String,
                               val properties: Array<Property>,
                               val constructorStrategies: Array<ConstructorStrategy>,
                               val propertyIdLong: (KProperty1<*, Long>)?,
                               val idLongNull: Long,
                               val propertyIdString: (KProperty1<*, String>)?,
                               val selectors: List<Selector>?) {
            fun hasPropertyId(): Boolean = (hasPropertyIdLong()) || (hasPropertyIdString())
            fun hasPropertyIdLong(): Boolean = propertyIdLong != null
            fun hasPropertyIdString(): Boolean = propertyIdString != null
        }

        private val metaInfo = mutableMapOf<String, MetaInfo>()  //String = class name

        class AdapterRule<T: Any> internal constructor(private var properties: MutableList<Property>,
                                                  internal var entityKind: String,
                                                  internal var selectors: MutableList<Selector>) {
            fun kind(value: String) {
                entityKind = value
            }

            fun <R> rename(property: KProperty1<T, R>, newName: String) {
                val name = property.name
                properties.first {
                    it.nameInClass == name
                }.nameInEntity = newName
            }

            fun <R> ignore(property: KProperty1<T, R>) {
                removeProperty(property.name)
            }

            fun <R> unindexed(property: KProperty1<T, R>) {
                val name = property.name
                properties.first {
                    it.nameInClass == name
                }.indexed = false
            }

            fun <R> indexed(property: KProperty1<T, R>) {
                val name = property.name
                properties.first {
                    it.nameInClass == name
                }.indexed = true
            }

            fun <R, S> convert(property: KProperty1<T, R>, get: (R) -> ValueBuilder<S, *, *>, set: (Value<S>) -> Any) {
                val name = property.name
                val prop = properties.first {
                    it.nameInClass == name
                }
                @Suppress("UNCHECKED_CAST")
                prop.getConverter = get as (Any) -> ValueBuilder<*, *, *>
                @Suppress("UNCHECKED_CAST")
                prop.setConverter = set as (Value<*>) -> Any
            }

            fun selector(clazz: KClass<out T>, value: (BaseEntity<Key>) -> Boolean) {
                selectors.add(Selector(clazz, value))
            }

            private fun removeProperty(name: String) {
                val i = properties.indexOfFirst {
                    it.nameInClass == name
                }
                if (i != -1) {
                    properties.removeAt(i)
                }
            }
        }

        private fun typeToValueBuilder(classifier: KClassifier?): (Any) -> ValueBuilder<*, *, *> {
            return when (classifier) {
                String::class -> { value -> StringValue.newBuilder(value as String) }
                Float::class  -> { value -> DoubleValue.newBuilder((value as Float).toDouble()) }
                Double::class -> { value -> DoubleValue.newBuilder(value as Double) }
                Boolean::class -> { value -> BooleanValue.newBuilder(value as Boolean) }
                Timestamp::class -> { value -> TimestampValue.newBuilder(value as Timestamp) }
                LocalDateTime::class -> { value ->
                    val v = value as LocalDateTime
                    TimestampValue.newBuilder(Timestamp.ofTimeSecondsAndNanos(
                            v.toEpochSecond(ZoneOffset.UTC),
                            v.nano))
                }
                LocalDate::class -> { value ->
                    val v = (value as LocalDate).atStartOfDay()
                    TimestampValue.newBuilder(Timestamp.ofTimeSecondsAndNanos(
                            v.toEpochSecond(ZoneOffset.UTC),
                            v.nano))
                }
                LocalTime::class -> { value ->
                    val v = (value as LocalTime).atDate(LocalDate.of(1970, 1, 1))
                    TimestampValue.newBuilder(Timestamp.ofTimeSecondsAndNanos(
                            v.toEpochSecond(ZoneOffset.UTC),
                            v.nano))
                }
                LatLng::class -> { value -> LatLngValue.newBuilder(value as LatLng) }
                Key::class -> { value -> KeyValue.newBuilder(value as Key) }
                Blob::class -> { value -> BlobValue.newBuilder(value as Blob) }
                ByteArray::class -> { value -> BlobValue.newBuilder(Blob.copyFrom(value as ByteArray)) }
                ByteBuffer::class -> { value -> BlobValue.newBuilder(Blob.copyFrom(value as ByteBuffer)) }
                InputStream::class -> { value -> BlobValue.newBuilder(Blob.copyFrom(value as InputStream)) }
                else -> {
                    if ((classifier as KClass<*>).isSubclassOf(Number::class)) {
                        { value -> LongValue.newBuilder((value as Number).toLong()) }
                    }
                    else {
                        { _ -> NullValue.newBuilder().setExcludeFromIndexes(true) }
                    }
                }
            }
        }

        fun <T: Any> register(clazz: KClass<*>, propertyIdLong: (KProperty1<*, Long>)?, idLongNull: Long,
                         propertyIdString: (KProperty1<*, String>)?, initialization: (AdapterRule<T>.() -> Unit)?) {
            if (clazz.visibility != KVisibility.PUBLIC) {
                throw ClassMustBePublicException(clazz)
            }
            var properties = mutableListOf<Property>()
            clazz.memberProperties.forEach {
                val getConverter = typeToValueBuilder(it.returnType.classifier)
                val setConverter: (Value<*>) -> Any = { value -> value.get() }
                val propertyClassName = it.name
                properties.add(Property(propertyClassName, propertyClassName,
                        it.getter, (it as? KMutableProperty<*>)?.setter, getConverter, setConverter,
                        false))
            }

            var kind = clazz.simpleName as String
            var selectors: List<Selector>? = null
            if (initialization != null) {
                val rule = AdapterRule<T>(properties, kind, mutableListOf())
                rule.initialization()
                kind = rule.entityKind
                var ruleSelectors = rule.selectors
                if (ruleSelectors.isNotEmpty()) {
                    selectors = ruleSelectors
                }
            }

            val id = propertyIdLong?.name ?: propertyIdString?.name
            var firstPropertyIsId = if (id == null) {
                false
            } else {
                var found = false
                if (properties[0].nameInClass != id) {
                    for (i in 1 until properties.size) {
                        if (properties[i].nameInClass == id) {
                            val temp = properties[0]
                            properties[0] = properties[i]
                            properties[i] = temp
                            found = true
                            break
                        }
                    }
                }
                found
            }

//            properties.forEach {
//                println("Property ${it.nameInClass}, in entity is ${it.nameInEntity} with getter ${it.getter} amd setter ${it.setter}")
//            }

            val constructorStrategies: MutableList<ConstructorStrategy> = mutableListOf()
            val size = properties.size
            clazz.constructors.forEach constructorLoop@{ constructor ->
                val parameters = constructor.parameters
                val constructorTableRow = BooleanArray(size)
                val constructorParameters = Array<KParameter?>(size) { null }
                if (parameters.isEmpty()) {
                    //Empty constructor
                } else {
                    val constructorTableRowWithOptional = BooleanArray(size)
                    var hasOptional = false
                    constructor.parameters.forEach { constructorParameter ->
                        val constructorParameterName = constructorParameter.name
                        val i = properties.indexOfFirst {
                            it.nameInClass == constructorParameterName
                        }
                        val isOptional = constructorParameter.isOptional
                        if (i != -1) {
                            constructorTableRow[i] = true
                            constructorParameters[i] = constructorParameter
                            if (isOptional) {
                                hasOptional = true
                                constructorTableRowWithOptional[i] = false
                            } else {
                                constructorTableRowWithOptional[i] = true
                            }
                        } else {
                            if (!isOptional) {
                                return@constructorLoop
                            }
                        }
                    }
                    if (hasOptional) {
                        constructorStrategies.add(ConstructorStrategy(constructor, constructorParameters, constructorTableRowWithOptional))
                    }
                }
                constructorStrategies.add(ConstructorStrategy(constructor, constructorParameters, constructorTableRow))
            }

            val ordered = constructorStrategies.sortedByDescending {
                // If the first column is the id parameter, constructors with required id parameter
                // has preference to other constructors without id parameter.
                // The initial fold value will be 100_000 if the first column (id parameter) is required. This gives
                // more preference to constructors with required id parameter.
                // More parameters in constructor, more preference. The score is increased by 100 * parameters size.
                it.table.fold(if (it.table[0] && firstPropertyIsId) 100_000 else 0) { score, element -> score + if (element) 1 else 0 } +
                        it.constructor.parameters.size * 100
            }
//            for (strategy in constructorStrategies) {
//                println("${strategy.table.joinToString()} ${strategy.constructor}")
//            }
//            println("Ordered:")
//            for (strategy in ordered) {
//                println("${strategy.table.joinToString()} ${strategy.constructor}")
//            }

            // remove duplicates
            val finalTable = ordered.distinctBy {
                it.table.joinToString(separator = "", transform = { value -> if (value) "1" else "0" })
            }
//            println("Final:")
//            for (strategy in finalTable) {
//                println("${strategy.table.joinToString()} ${strategy.constructor}")
//            }

            val name = clazz.qualifiedName as String
            metaInfo[name] = MetaInfo(clazz, kind, properties.toTypedArray(), finalTable.toTypedArray(), propertyIdLong, idLongNull, propertyIdString, selectors)
        }

        inline fun <reified T: Any> register(idProperty: KProperty1<T, String>, noinline initialization: (AdapterRule<T>.() -> Unit)? = null) {
            register(T::class, null, defaultIdNullLong, idProperty, initialization)
        }

        inline fun <reified T: Any> register(idProperty: KProperty1<T, Long>, idNull: Long = defaultIdNullLong, noinline initialization: (AdapterRule<T>.() -> Unit)? = null) {
            register(T::class, idProperty, idNull, null, initialization)
        }

        inline fun <reified T: Any> register(noinline initialization: (AdapterRule<T>.() -> Unit)? = null) {
            register(T::class, null, defaultIdNullLong, null, initialization)
        }

        inline fun <reified T> fromEntity(entity: BaseEntity<Key>): T {
            return fromEntity(T::class, entity) as T
        }

        fun fromEntity(clazz: KClass<*>, entity: BaseEntity<Key>): Any {
            var classMetaInfo = metaInfo.values.firstOrNull {
                it.clazz == clazz
            } ?: throw ClassNotFoundForEntityException(clazz.toString())
            val selectors = classMetaInfo.selectors
            if (selectors != null) {
                for (selector in selectors) {
                    if (selector.selector.invoke(entity)) {
                        classMetaInfo = metaInfo.values.firstOrNull {
                            it.clazz == selector.clazz
                        } ?: throw ClassNotFoundForEntityException(selector.clazz.toString())
                        break
                    }
                }
            }
            val registeredProperties = classMetaInfo.properties
            val registeredPropertiesInEntity = BooleanArray(registeredProperties.size)
            val entityPropertiesNames = entity.names.toMutableList()
            var iniI = if (classMetaInfo.hasPropertyId()) {
                            registeredPropertiesInEntity[0] = true
                            1
                        }
                        else 0
            for (i in iniI until registeredProperties.size) {
                registeredPropertiesInEntity[i] = entityPropertiesNames.contains(registeredProperties[i].nameInEntity)
            }
            classMetaInfo.constructorStrategies.forEach { constructorStrategy ->
                val table = constructorStrategy.table
                if (table.equalOrs(registeredPropertiesInEntity)) {
                    val constructor = constructorStrategy.constructor
                    val constructorParameters = constructorStrategy.constructorParameters
                    val parameters = mutableMapOf<KParameter, Any?>()
                    for (i in iniI until table.size) {
                        if (table[i]) {
//                        if (registeredPropertiesInEntity[i]) {
                            val entityPropertyName = registeredProperties[i].nameInEntity
                            val value = entity.getValue<Value<*>>(entityPropertyName).get()
                            if (value is List<*>) {
                                val list = mutableListOf<Any?>()
                                for (item in value) {
                                    if (item is EntityValue) {
                                        @Suppress("UNCHECKED_CAST")
                                        var entityItem = item.get() as FullEntity<Key>
                                        var kind = entityItem.key.kind
                                        val classMetaInfoItem = metaInfo.values.firstOrNull {
                                            it.entityKind == kind
                                        } ?: throw ClassNotFoundForEntityException(kind)
                                        var itemClazz = classMetaInfoItem.clazz
                                        val itemValue = fromEntity(itemClazz, entityItem)
                                        list.add(itemValue)
                                    }
                                }
                                parameters[constructorParameters[i]!!] = list
                            }
                            else {
                                if (value is Number) {
                                    //In DataStore, all the numbers are stored like Long or Double. Convert the
                                    //value to parameter expected
                                    when (constructorParameters[i]!!.type.classifier) {
                                        Long::class -> parameters[constructorParameters[i]!!] = value.toLong()
                                        Int::class -> parameters[constructorParameters[i]!!] = value.toInt()
                                        Double::class -> parameters[constructorParameters[i]!!] = value.toDouble()
                                        Float::class -> parameters[constructorParameters[i]!!] = value.toFloat()
                                        Byte::class -> parameters[constructorParameters[i]!!] = value.toByte()
                                        Short::class -> parameters[constructorParameters[i]!!] = value.toShort()
                                        else -> parameters[constructorParameters[i]!!] = value
                                    }
                                }
                                else {
                                    parameters[constructorParameters[i]!!] = value
                                }
                            }
                            entityPropertiesNames.remove(entityPropertyName)
                        }
                    }
                    if ((iniI == 1) && (table[0]))  { // has property id
                        parameters[constructorParameters[0]!!] = entity.key.id ?: entity.key.name
                    }
                    val instance = constructor.callBy(parameters)
                    for (i in iniI until table.size) {
                        if ((!table[i]) && (registeredPropertiesInEntity[i]))  {
                            val entityPropertyName = registeredProperties[i].nameInEntity
                            val setter = registeredProperties[i].setter
                            if (setter != null) {
                                val entityValue = entity.getValue<Value<*>>(entityPropertyName)
                                if (entityValue != null) {
                                    val convertedValue = registeredProperties[i].setConverter.invoke(entityValue)
                                    setter.call(instance, convertedValue)
                                }
                            }
                        }
                    }
                    return instance
                }
            }
            throw CantConstructClassFromEntityException(entity, clazz.toString())
        }

        fun kind(clazz: KClass<*>): String? {
            val name = clazz.qualifiedName as String
            return metaInfo[name]?.entityKind
        }

        fun toFullEntityList(iterable: Iterable<*>, parent: Key? = null): List<FullEntity<*>> {
            return toFullEntityList(DatastoreOptions.getDefaultInstance().service, iterable, parent)
        }

        fun toFullEntityList(datastore: Datastore, iterable: Iterable<*>, parent: Key? = null): List<FullEntity<*>> {
            val result = mutableListOf<FullEntity<*>>()
            for (obj in iterable) {
                if (obj != null) {
                    val entity = toFullEntity(datastore, obj, parent)
                    result.add(entity)
                }
            }
            return result
        }

        fun toFullEntity(obj: Any, parent: Key? = null): FullEntity<*> {
            return toFullEntity(DatastoreOptions.getDefaultInstance().service, obj, parent)
        }

        fun toEntity(datastore: Datastore, obj: Any, parent: Key? = null): Entity {
            return toFullEntity(datastore, obj, parent) as Entity
        }

        fun toFullEntity(datastore: Datastore, obj: Any, parent: Key? = null): FullEntity<*> {
            val className = obj::class.qualifiedName ?: obj::class.java.name
            val entityMetaInfo = metaInfo[className] ?: throw ClassNotRegisteredException(className)
            val entityKind = entityMetaInfo.entityKind
            val keyFactory = datastore.newKeyFactory().setKind(entityKind)
            if (parent != null) {
                keyFactory.addAncestor(PathElement.of(parent.kind, parent.id))
            }

            val entityBuilder =
                    if (entityMetaInfo.hasPropertyId()) {
                        if (entityMetaInfo.hasPropertyIdLong()) {
                            val value = entityMetaInfo.propertyIdLong?.call(obj)
                            if ((value == null) || (value == entityMetaInfo.idLongNull)) {
                                val key = keyFactory.newKey()
                                FullEntity.newBuilder(key)
                            }
                            else {
                                val key = keyFactory.newKey(value.toLong())
                                Entity.newBuilder(key)
                            }
                        } else {
                            val value = entityMetaInfo.propertyIdString?.call(obj)
                            if (value.isNullOrEmpty()) {
                                val key = keyFactory.newKey()
                                FullEntity.newBuilder(key)
                            }
                            else {
                                val key = keyFactory.newKey(value)
                                Entity.newBuilder(key)
                            }
                        }
                    } else {
                        val key = keyFactory.newKey()
                        FullEntity.newBuilder(key)
                    }

            val iniI = if (entityMetaInfo.hasPropertyId()) 1 else 0
            val properties = entityMetaInfo.properties
            for (i in iniI until properties.size) {
                val property = properties[i]
                val getterValue = property.getter.call(obj)
                if (getterValue != null) {
                    if (getterValue is Iterable<*>) {
                        val listValues = mutableListOf<Value<*>>()
                        for (child in getterValue) {
                            val entity = toFullEntity(datastore, child!!)
                            val entityValue = EntityValue.newBuilder(entity).setExcludeFromIndexes(true).build()
                            listValues.add(entityValue)
                        }
                        if (listValues.isNotEmpty()) {
                            val nameInEntity = property.nameInEntity
                            entityBuilder.set(nameInEntity, listValues)
                        }
                    }
                    else {
                        val valueBuilder = property.getConverter.invoke(getterValue)
                        valueBuilder.setExcludeFromIndexes(!property.indexed)
                        val nameInEntity = property.nameInEntity
                        val entityValue = valueBuilder.build()
                        entityBuilder.set(nameInEntity, entityValue)
                    }
                }
            }
            return if (entityBuilder is Entity.Builder) {
                    entityBuilder.build()
                }
                else {
                    (entityBuilder as FullEntity.Builder<*>).build()
                }
        }
    }
}

fun BooleanArray.equalOrs(other: BooleanArray): Boolean {
    for (i in 0 until size) {
        if (get(i) && (!other[i])) {
            return false
        }
    }
    return true
}