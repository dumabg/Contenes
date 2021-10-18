package com.augdim.backend.gcd

class ClassNotFoundForEntityException(entityName: String):
    Exception("Not found class for $entityName entity. Register the class with EntityAdapter.register<$entityName>().")