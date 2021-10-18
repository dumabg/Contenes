package com.augdim.backend.gcd

import kotlin.reflect.KClass

class ClassMustBePublicException(clazz: KClass<*>):
        Exception("Class ${clazz.qualifiedName ?: clazz.java.name} must be PUBLIC")